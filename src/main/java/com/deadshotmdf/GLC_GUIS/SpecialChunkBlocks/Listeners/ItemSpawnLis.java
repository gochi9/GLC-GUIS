package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Listeners;

import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.ChunkHopper;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocksManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

public class ItemSpawnLis implements Listener {

    private final SpecialBlocksManager specialBlocksManager;

    public ItemSpawnLis(SpecialBlocksManager specialBlocksManager) {
        this.specialBlocksManager = specialBlocksManager;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onItemSpawn(ItemSpawnEvent ev) {
        Location location = ev.getLocation();
        ChunkHopper hopper = specialBlocksManager.getChunkCollector(location);

        if(hopper == null)
            return;

        if(!specialBlocksManager.addItem(hopper, ev.getEntity().getItemStack(), location))
            return;

        ev.setCancelled(true);
        ev.getEntity().remove();
    }

}
