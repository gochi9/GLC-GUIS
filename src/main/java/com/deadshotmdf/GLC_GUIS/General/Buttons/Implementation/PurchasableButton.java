package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation;

import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PurchasableButton extends AbstractButton {

    public PurchasableButton(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args) {
        super(item, correspondentManager, guiManager, args);
    }

    @Override
    public void onClick(InventoryClickEvent event, GUI gui, Object... args) {

    }

}
