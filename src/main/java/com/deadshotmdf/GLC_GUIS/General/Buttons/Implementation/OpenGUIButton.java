package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation;

import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.CommandIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@CommandIdentifier("OPEN_GUI")
public class OpenGUIButton extends AbstractButton {

    private final String guiName;

    public OpenGUIButton(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args) {
        super(item, correspondentManager, guiManager, args);
        this.guiName = args.length > 0 ? args[0] : "shop";
    }

    @Override
    public void onClick(InventoryClickEvent event, GUI gui, Object... args) {
        guiManager.openGui((Player) event.getWhoClicked(), guiName);
    }

}
