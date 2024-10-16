package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.Mayor;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GLCGGUIS;
import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.GLC_GUIS.Mayor.*;
import com.deadshotmdf.gLCoins_Server.EconomyWrapper;
import com.deadshotmdf.gLCoins_Server.GLCoinsS;
import com.deadshotmdf.glccoinscommon.ModifyType;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@ButtonIdentifier("MAYOR_UPGRADE_BUTTON")
public class MayorUpgrade extends AbstractButton {

    private final MayorManager mayorManager;
    private final UpgradeType upgradeType;

    public MayorUpgrade(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
        this.mayorManager = (MayorManager) correspondentManager;
        this.upgradeType = args.length > 0 ? UpgradeType.fromString(args[0]) : null;
    }

    public UpgradeType getUpgradeType() {
        return upgradeType;
    }

    public ItemStack getMayorItem(Upgrade upgrade, Map<UpgradeType, Integer> map, DelayUpgradePair delayUpgradePair){
        ItemStack item = getItemStackClone();

        if(upgradeType == null || upgrade == null)
            return item;

        ItemMeta meta = item.getItemMeta();

        if(meta == null)
            return null;

        List<String> main_lore = upgrade.getMain_lore(), upgrade_lore = upgrade.getUpgrade_lore();
        int currentLevel = map.getOrDefault(upgradeType, 0);
        int upgrade_lore_size = upgrade_lore.size();
        List<String> newLore = new ArrayList<>(main_lore.size() + upgrade_lore_size + 2), upgrade_lore_clone = new ArrayList<>(upgrade_lore_size);
        newLore.addAll(main_lore);
        newLore.add(" ");

        String[] placeholders = {"{price_cash}", "{price_glcoins}", "{delay}", "{benefit}"};
        for(int i = 1; i <= upgrade.getMaxLevel(); i++) {
            UpgradeLevel level;

            try{level = upgrade.getLevels().get(i - 1);}
            catch (Throwable ignored){break;}

            for (String s : upgrade_lore)
                upgrade_lore_clone.add(StringUtils.replaceEach(ConfigSettings.color(((currentLevel == i) ? ChatColor.GREEN : ChatColor.GRAY) + s), placeholders, new String[]{String.valueOf(level.getCost()), String.valueOf(level.getGlcoins()), TimeUnit.MILLISECONDS.toMinutes(level.getDelay()) + " minutes", String.valueOf(level.getBenefit())}));
        }

        if(currentLevel > upgrade.getMaxLevel())
            upgrade_lore_clone.set(upgrade_lore_size -= 1, ChatColor.GREEN + upgrade_lore.get(upgrade_lore_size));

        newLore.addAll(upgrade_lore_clone);

        if(delayUpgradePair != null && delayUpgradePair.upgradeType() == upgradeType){
            newLore.add(" ");
            newLore.add(ConfigSettings.getUpgradeInProgress(delayUpgradePair.delay(), upgrade.getLevels().get(Math.max(currentLevel - 1, 0)).getInstant_unlock()));
        }

        meta.setLore(newLore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void onClick(InventoryClickEvent ev, GUI gui, Object... args) {
        if(upgradeType == null)
            return;

        UUID uuid = ev.getWhoClicked().getUniqueId();
        if(mayorManager.noLongerDelay(uuid)){
            gui.refreshInventory();
            return;
        }

        Upgrade upgrade = mayorManager.getUpgrade(upgradeType);

        if(upgrade == null)
            return;

        int playerLevel = mayorManager.getPlayerUpgrade(uuid, upgradeType);

        if(playerLevel >= upgrade.getMaxLevel())
            return;

        UpgradeLevel nextLevel;

        //Player level is from 1 to infinity
        //Upgrade level list is from 0 to infinity
        //To access the current level of a player you would do player level - 1
        try{nextLevel = upgrade.getLevels().get(playerLevel);}
        catch (Throwable ignored){return;}

        DelayUpgradePair delayUpgradePair = mayorManager.getDelayedUpgrade(uuid);
        boolean delayExits = delayUpgradePair != null;

        if(delayExits && delayUpgradePair.upgradeType() != upgradeType)
            return;

        EconomyWrapper economy = GLCGGUIS.getEconomy();
        Player player = (Player) ev.getWhoClicked();
        switch (ev.getClick()){
            case LEFT:
                if(delayExits)
                    return;

                double cost = nextLevel.getCost();
                double balance = economy.getBalance(player);
                if(balance < cost){
                    player.sendMessage(ConfigSettings.getNotEnoughFunds(cost, balance));
                    return;
                }

                economy.withdrawPlayer(player, cost);
                player.sendMessage(ConfigSettings.getBoughtItem(cost, 0));
                mayorManager.startPlayerDelay(upgrade, uuid, upgradeType, playerLevel);
                gui.refreshInventory();
                return;

            case RIGHT: GLCoinsS.getDatabase().getEntryPartialAsync(uuid, uuid, null).thenAccept(value ->{
                if(value == null)
                    return;

                double glcoins = !delayExits ? nextLevel.getGlcoins() : nextLevel.getInstant_unlock();

                if(value < glcoins){
                    player.sendMessage(ConfigSettings.getNotEnoughFunds(glcoins, value));
                    return;
                }

                GLCoinsS.getDatabase().modifyEntryAsync(uuid, glcoins, ModifyType.REMOVE, null);
                mayorManager.removeDelayedUpgrade(uuid);
                mayorManager.advancePlayerLevel(uuid, upgradeType);
                player.sendMessage(ConfigSettings.getBoughtItem(0, glcoins));
                gui.refreshInventory();
            });

        }
    }
}
