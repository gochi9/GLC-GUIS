package com.deadshotmdf.GLC_GUIS.General.GUI;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public interface GUI {

    boolean isShared();
    void handleClick(InventoryClickEvent ev);
    void handleClose(InventoryCloseEvent ev);
    void refreshInventory();
    public GUI createInstance(UUID player, GUI backGUI, String... args);
    void open(Player player, int page);
    int getPageCount();
    void deletePages();
    int getPageByInventory(Player player);
    int getPageByInventory(Inventory inventory);
    void setBackGUI(GUI backGUI);
    GUI getBackGUI();

}
