package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.SpecialChunkBlocks;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@ButtonIdentifier("COLLECTOR_ITEM_DISPLAY")
public class CollectorItemDisplay extends AbstractButton {


    public CollectorItemDisplay(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
    }

    public void setInfo(Material material, int amount, int maxAmount, double sell) {
        this.name = ConfigSettings.getChunkCollectorItemName(GUIUtils.formatItemName(material.toString()), amount, maxAmount);
        this.lore = ConfigSettings.getChunkCollectorItemLore(sell);
        this.item = getItemStackClone(null);
    }

    @Override
    public void onClick(InventoryClickEvent event, GUI gui, Object... args) {

    }
}
