package com.deadshotmdf.GLC_GUIS.General.Buttons;

import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface GuiElement {

    void addExtra(String... extra);
    ItemStack getItemStackClone(String[] placeholder, String... replace);
    boolean canClick(HumanEntity player);
    void onClick(InventoryClickEvent ev, GUI gui, Object... args);

}
