package com.deadshotmdf.GLC_GUIS.Test;

import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AHTransactionButton extends AbstractButton {

    private final double sellAmount;
    private final double GLCSellAmount;

    public AHTransactionButton(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData, double sellAmount, double GLCSellAmount) {
        super(item, correspondentManager, guiManager, args, elementData);
        this.sellAmount = sellAmount;
        this.GLCSellAmount = GLCSellAmount;
    }

    @Override
    public void onClick(InventoryClickEvent event, GUI gui, Object... args) {

    }

}
