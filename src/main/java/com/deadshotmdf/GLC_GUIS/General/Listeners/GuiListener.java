package com.deadshotmdf.GLC_GUIS.General.Listeners;

import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GUIListener implements Listener {

    private final GuiManager guiManager;

    public GUIListener(GuiManager guiManager) {
        this.guiManager = guiManager;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onClick(InventoryClickEvent ev) {
        handle((Player) ev.getWhoClicked(), ev, TypeAction.CLICK);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onClick(InventoryCreativeEvent ev) {
        handle((Player) ev.getWhoClicked(), ev, TypeAction.CLICK);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onClose(InventoryCloseEvent ev) {
        handle((Player) ev.getPlayer(), ev, TypeAction.CLOSE);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent ev) {
        handle(ev.getPlayer(), ev, TypeAction.LEAVE);
    }

    private void handle(Player player, Event ev, TypeAction action){
        GUI gui = guiManager.getOpenGui(player.getUniqueId());

        if(gui == null)
            return;

        if(ev instanceof Cancellable cancellable)
            cancellable.setCancelled(true);

        switch(action){
            case CLICK:
                gui.handleClick((InventoryClickEvent) ev);
                break;
            case CLOSE:
                gui.handleClose((InventoryCloseEvent) ev);
                break;
            case LEAVE:
                guiManager.removeOpenGui(player, false);
                break;
        }
    }

}

enum TypeAction{

    CLICK,
    CLOSE,
    LEAVE;

}