package com.deadshotmdf.GLC_GUIS.General.Buttons;

import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public interface GuiElement {

    void addExtra(String... extra);
    ItemStack getItemStackClone();
    ItemStack getItemStackClone(String[] placeholder, String... replace);
    ItemStack getItemStackClone(ItemStack clone, String[] placeholder, String... replace);
    ItemStack getItemStackMarkedAndReplaced(NamespacedKey key, PersistentDataType type, Object value, String[] placeholders, String... replace);
    void addInitialMark(NamespacedKey key, PersistentDataType type, Object value);
    boolean canClick(HumanEntity player);
    void onClick(InventoryClickEvent ev, GUI gui, Object... args);

}
