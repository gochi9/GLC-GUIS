package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.Mayor;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.GLC_GUIS.Mayor.DelayUpgradePair;
import com.deadshotmdf.GLC_GUIS.Mayor.Upgrade;
import com.deadshotmdf.GLC_GUIS.Mayor.UpgradeType;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ButtonIdentifier("MAYOR_UPGRADE_BUTTON")
public class MayorUpgrade extends AbstractButton {

    private final UpgradeType upgradeType;

    public MayorUpgrade(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
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

        int i = 1;
        for(String s : upgrade_lore)
            upgrade_lore_clone.add(ConfigSettings.color(((currentLevel == i++) ? ChatColor.GREEN : ChatColor.GRAY) + s));

        if(currentLevel > upgrade.getMaxLevel())
            upgrade_lore_clone.set(upgrade_lore_size -= 1, ChatColor.GREEN + upgrade_lore.get(upgrade_lore_size));

        newLore.addAll(upgrade_lore_clone);

        if(delayUpgradePair != null && delayUpgradePair.upgradeType() == upgradeType){
            newLore.add(" ");
            newLore.add(ConfigSettings.getUpgradeInProgress(delayUpgradePair.delay()));
        }

        meta.setLore(newLore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void onClick(InventoryClickEvent event, GUI gui, Object... args) {

    }
}
