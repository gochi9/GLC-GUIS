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
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@ButtonIdentifier("COLLECTOR_ITEM_DISPLAY")
public class CollectorItemDisplay extends AbstractButton {

    private final SpecialBlocksManager specialBlocksManager;
    private ChunkHopper chunkHopper;

    public CollectorItemDisplay(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
        this.specialBlocksManager = (SpecialBlocksManager) correspondentManager;
    }

    public void setInfo(Material material, int amount, int maxAmount, double sell) {
        this.name = ConfigSettings.getChunkCollectorItemName(GUIUtils.formatItemName(material.toString()), amount, maxAmount);
        this.lore = ConfigSettings.getChunkCollectorItemLore(sell);
        this.item = getItemStackClone(null);
    }

    @Override
    public void onClick(InventoryClickEvent ev, GUI gui, Object... args) {
        if(chunkHopper == null && args.length == 0)
            return;

        if(args[0] instanceof ChunkHopper hopper)
            this.chunkHopper = hopper;

        if(chunkHopper == null || item == null)
            return;

        Material material = item.getType();
        int amount = chunkHopper.getValues().get(material);

        if(amount == 0)
            return;

        Player player = (Player) ev.getWhoClicked();
        switch (ev.getClick()){
            case LEFT:
                specialBlocksManager.addPlayerItem(player, player.getLocation(), new ItemStack(material, Math.min(amount, 64)));
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
