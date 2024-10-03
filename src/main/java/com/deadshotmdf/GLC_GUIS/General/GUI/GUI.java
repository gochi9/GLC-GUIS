package com.deadshotmdf.GLC_GUIS.General.GUI;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.UUID;

public interface GUI {

    boolean isShared();
    void handleClick(InventoryClickEvent ev);
    void handleClose(InventoryCloseEvent ev);
    void refreshInventory();
    GUI createInstance(UUID player);
    void open(Player player, int page);
    int getPageCount();
    void deletePages();

}
