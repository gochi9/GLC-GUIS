package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.Generic;

import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@ButtonIdentifier("OPEN_GUI")
public class OpenGUIButton extends AbstractButton {

    private final String guiName;

    public OpenGUIButton(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
        this.guiName = args.length > 0 ? args[0] : "shop";
    }

    @Override
    public void onClick(InventoryClickEvent event, GUI gui, Object... args) {
        guiManager.openGui(event.getWhoClicked(), guiName, gui);
    }

}
