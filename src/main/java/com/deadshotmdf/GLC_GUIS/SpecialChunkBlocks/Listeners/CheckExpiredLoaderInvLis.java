package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Listeners;

import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocksManager;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class CheckExpiredLoaderInvLis implements Listener {

    private final SpecialBlocksManager chunkLoaderManager;

    public CheckExpiredLoaderInvLis(SpecialBlocksManager chunkLoaderManager) {
        this.chunkLoaderManager = chunkLoaderManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent ev) {
        ItemStack clickedItem = ev.getCurrentItem();

        if(clickedItem == null)
            return;

        if(!(chunkLoaderManager.isLoaderItemExpired((Player) ev.getWhoClicked(), clickedItem)))
            return;

        ev.setCancelled(true);
        ev.setCurrentItem(null);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent ev) {
        Item item = ev.getItemDrop();
        ItemStack clickedItem = item.getItemStack();

        if(!(chunkLoaderManager.isLoaderItemExpired(ev.getPlayer(), clickedItem)))
            return;

        item.setPickupDelay(Integer.MAX_VALUE);
        item.remove();
    }

}
