package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.AH;

import com.deadshotmdf.GLC_GUIS.AH.AHManager;
import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@ButtonIdentifier("AH_OPEN")
public class AHNavigation extends AbstractButton {

    private final AHManager ahManager;
    private final boolean isMain;

    public AHNavigation(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
        this.ahManager = (AHManager) correspondentManager;
        this.isMain = args == null || args.length == 0 || args[0].equalsIgnoreCase("ah");
    }

    @Override
    public void onClick(InventoryClickEvent event, GUI gui, Object... args) {
        ahManager.openGUI(event.getWhoClicked(), isMain);
    }
}
