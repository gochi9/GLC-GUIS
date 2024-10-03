package com.deadshotmdf.GLC_GUIS.General.Buttons;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface GuiElement {

    ButtonType getButtonType();
    ItemStack getItemStackClone(String[] placeholder, String... replace);
    void onClick(InventoryClickEvent event, Object... args);

}
