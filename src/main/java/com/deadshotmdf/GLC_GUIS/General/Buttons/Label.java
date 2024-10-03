package com.deadshotmdf.GLC_GUIS.General.Buttons;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

//Default button type in case one wasn't present in the config
public class Label extends AbstractButton {

    public Label(ItemStack itemStack) {
        super(itemStack, null);
    }

    @Override
    public ButtonType getButtonType() {
        return ButtonType.LABEL;
    }

    @Override
    public void onClick(InventoryClickEvent event, Object... args) {
        // Do jack shit, labels are not clickable.
    }
}

