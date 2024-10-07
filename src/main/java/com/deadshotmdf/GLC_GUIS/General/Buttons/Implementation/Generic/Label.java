package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.Generic;

import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

//Default button type in case one wasn't present in the config
@ButtonIdentifier("LABEL")
public class Label extends AbstractButton {

    public Label(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, null, null, null, null);
    }

    @Override
    public void onClick(InventoryClickEvent event, GUI gui, Object... args) {
        // Do jack shit, labels are not clickable.
    }
}

