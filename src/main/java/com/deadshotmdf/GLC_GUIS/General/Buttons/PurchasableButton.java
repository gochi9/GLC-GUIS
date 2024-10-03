package com.deadshotmdf.GLC_GUIS.General.Buttons;

import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PurchasableButton extends AbstractButton{

    private ItemStack placeholder;

    public PurchasableButton(ItemStack placeholder, Object mngr) {
        super(placeholder, mngr);
        this.placeholder = placeholder;
    }

    @Override
    public ButtonType getButtonType() {
        return null;
    }

    @Override
    public void onClick(InventoryClickEvent event, GUI gui, Object... args) {

    }

    public void setPlaceholder(ItemStack placeholder) {
        this.placeholder = placeholder;
    }

}
