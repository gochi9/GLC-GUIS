package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation;

import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@ButtonIdentifier("FILLER")
public class Filler extends AbstractButton {

    public Filler(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, null, null, null, null);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setHideTooltip(true);
        item.setItemMeta(itemMeta);
    }

    //Filler doesn't have any name or lore visible so there's no need to keep the parent implementation
    @Override
    public ItemStack getItemStackClone(String[] placeholder, String... replace){ return this.item.clone(); }

    @Override
    public void onClick(InventoryClickEvent event, GUI gui, Object... args) {
        // Do jack shit, fillers are not clickable.
    }
}
