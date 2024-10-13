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

@ButtonIdentifier("BACK")
public class BackButton extends AbstractButton {

    public BackButton(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
    }

    @Override
    public void onClick(InventoryClickEvent ev, GUI gui, Object... args) {
        if(gui.getBackGUI() != null)
            guiManager.commenceOpen(ev.getWhoClicked(), gui.getBackGUI(), gui.getBackGUI().getBackGUI());
        else
            guiManager.removeOpenGui(ev.getWhoClicked());
    }
}
