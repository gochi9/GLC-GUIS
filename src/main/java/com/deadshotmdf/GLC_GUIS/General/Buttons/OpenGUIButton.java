package com.deadshotmdf.GLC_GUIS.General.Buttons;

import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OpenGUIButton extends AbstractActionButton{

    private final String guiName;

    public OpenGUIButton(ItemStack item, Object correspondentManager, GuiManager guiManager, @NotNull String guiName) {
        super(item, guiManager, correspondentManager);
        this.guiName = guiName;
    }

    @Override
    public ButtonType getButtonType() {
        return ButtonType.OPEN_GUI;
    }

    @Override
    public void onClick(InventoryClickEvent event, Object... args) {
        guiManager.openGui((Player) event.getWhoClicked(), guiName);
    }

}
