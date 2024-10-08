package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.Blackmarket;

import com.deadshotmdf.GLC_GUIS.BlackMarket.Managers.BlackMarketManager;
import com.deadshotmdf.GLC_GUIS.BlackMarket.Objects.BlackmarketEvent;
import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GLCGGUIS;
import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.gLCoins_Server.EconomyWrapper;
import com.iridium.iridiumcolorapi.IridiumColorAPI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@ButtonIdentifier("BLACK_MARKET_BUY_ITEM")
public class BlackMarketItemButton extends AbstractButton {

    private final EconomyWrapper economy;
    private final BlackMarketManager blackMarketManager;
    private final double sell_value;
    private Material material;

    public BlackMarketItemButton(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
        this.economy = GLCGGUIS.getEconomy();
        this.blackMarketManager = (BlackMarketManager) correspondentManager;
        this.sell_value = GUIUtils.getDoubleOrDefault(elementData.get("sell_value"), 0.0);
        String mat = elementData.get("material");
        this.material = Material.getMaterial(mat != null ? mat.toUpperCase() : "DIRT");
        this.material = material != null ? this.material : Material.DIRT;

        this.item = getItemStackClone(new String[]{"{blackMarketItemName}", "{blackMarketItemLore}"},
                IridiumColorAPI.process(ConfigSettings.getBlackMarketItemName(GUIUtils.formatItemName(material.toString()))),
                IridiumColorAPI.process(ConfigSettings.getBlackMarketLore(sell_value)));
    }

    @Override
    public void onClick(InventoryClickEvent ev, GUI gui, Object... args) {
        if(!blackMarketManager.isBlackMarketOngoing() || sell_value <= 0.000)
            return;

        boolean isSellAll = ev.isShiftClick() && ev.isRightClick();
        Player player = (Player) ev.getWhoClicked();

        int totalAmount = getTotalMaterialAmount(player, material);

        if (totalAmount == 0)
            return;

        int amountRemoved = removeItemsFromInventory(player, material, isSellAll ? totalAmount : Math.min(64, totalAmount));

        if (amountRemoved == 0)
            return;

        double totalValue = amountRemoved * sell_value;
        BlackmarketEvent blackmarketEvent = blackMarketManager.isEvent();

        switch (blackmarketEvent) {
            case NOTHING:
                economy.noTaxDepositPlayer(player, totalValue);
                player.sendMessage(ConfigSettings.getSoldItemBlackmarket(amountRemoved, totalValue));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0F, 1.0F);
                break;

            case ROBBED:
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1.0F, 1.0F);
                player.sendMessage(ConfigSettings.getRobbed());
                break;

            case SCAMMED:
                double percentReceived = GUIUtils.getRandomDouble(ConfigSettings.getScammedMinPercent(), ConfigSettings.getScammedMaxPercent());
                double moneyReceived = totalValue * (percentReceived / 100.0);
                player.sendMessage(ConfigSettings.getSoldItemBlackmarket(amountRemoved, totalValue));
                economy.noTaxDepositPlayer(player, moneyReceived);
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
                String scammedMessage = ConfigSettings.getScammed(100.0 - percentReceived, totalValue - moneyReceived);
                player.sendMessage(scammedMessage);
                break;

            case MUGGED:
                player.getInventory().clear();
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 1.0F, 1.0F);
                player.sendMessage(ConfigSettings.getMugged());
                break;
        }
    }

    private int getTotalMaterialAmount(Player player, Material material) {
        int totalAmount = 0;
        for (ItemStack item : player.getInventory().getContents())
            if (item != null && item.getType() == material)
                totalAmount += item.getAmount();

        return totalAmount;
    }

    private int removeItemsFromInventory(Player player, Material material, int amountToRemove) {
        Inventory inventory = player.getInventory();
        int amountRemoved = 0;
        int amountLeftToRemove = amountToRemove;

        for (int i = 0; i < inventory.getSize(); i++) {
            if (amountLeftToRemove <= 0)
                break;

            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType() != material)
                continue;

            int itemAmount = item.getAmount();

            if (itemAmount <= amountLeftToRemove) {
                amountRemoved += itemAmount;
                amountLeftToRemove -= itemAmount;
                inventory.setItem(i, null);
                continue;
            }

            item.setAmount(itemAmount - amountLeftToRemove);
            inventory.setItem(i, item);
            amountRemoved += amountLeftToRemove;
            break;
        }
        return amountRemoved;
    }
}
