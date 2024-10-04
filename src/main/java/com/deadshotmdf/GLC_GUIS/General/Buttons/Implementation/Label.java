package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation;

import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.CommandIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

//Default button type in case one wasn't present in the config
@CommandIdentifier("LABEL")
public class Label extends AbstractButton {

    public Label(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args) {
        super(item, null, null, null);
    }

    @Override
    public void onClick(InventoryClickEvent event, GUI gui, Object... args) {
        // Do jack shit, labels are not clickable.
    }
}

