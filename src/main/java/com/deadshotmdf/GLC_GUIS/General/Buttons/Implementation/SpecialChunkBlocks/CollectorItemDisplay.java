package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.SpecialChunkBlocks;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GLCGGUIS;
import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.ChunkHopper;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocksManager;
import com.deadshotmdf.gLCoins_Server.EconomyWrapper;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@ButtonIdentifier("COLLECTOR_ITEM_DISPLAY")
public class CollectorItemDisplay extends AbstractButton {

    private final static String[] dummy = {"{t}"};

    private final SpecialBlocksManager specialBlocksManager;
    private ChunkHopper chunkHopper;

    public CollectorItemDisplay(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
        this.specialBlocksManager = (SpecialBlocksManager) correspondentManager;
    }

    public void setInfo(Material material, int amount, int maxAmount, double sell) {
        this.name = ConfigSettings.getChunkCollectorItemName(GUIUtils.formatItemName(material.toString()), amount, maxAmount);
        this.lore = ConfigSettings.getChunkCollectorItemLore(sell);
        this.item = getItemStackClone(dummy, dummy);
    }

    @Override
    public void onClick(InventoryClickEvent ev, GUI gui, Object... args) {
        if(chunkHopper == null && args.length < 2)
            return;

        if(args[1] instanceof ChunkHopper hopper)
            this.chunkHopper = hopper;

        if(chunkHopper == null || item == null)
            return;

        Material material = item.getType();
        Integer amount = chunkHopper.getValues().get(material);

        if(amount == null || amount == 0)
            return;

        Player player = (Player) ev.getWhoClicked();
        switch (ev.getClick()){
            case LEFT:
                int itemAmount = Math.min(amount, 64);
                specialBlocksManager.addPlayerItem(player, player.getLocation(), new ItemStack(material, itemAmount));
                chunkHopper.getValues().put(material, Math.max(amount - itemAmount, 0));
                break;
            case RIGHT:
                EconomyWrapper economy = GLCGGUIS.getEconomy();
                double price = specialBlocksManager.getPrices().getOrDefault(material, 0.000);

                if(price <= 0.000)
                    //Return so we don't just refresh the inventory for no reason
                    return;

                chunkHopper.getValues().put(material, 0);
                economy.depositPlayer((OfflinePlayer) ev.getWhoClicked(), (amount*1d) * price);
                //msg
                break;
        }

        gui.refreshInventory();
    }
}
