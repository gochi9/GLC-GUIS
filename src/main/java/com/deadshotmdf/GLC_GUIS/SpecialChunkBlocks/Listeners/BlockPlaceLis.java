package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Listeners;

import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.API.SpecialBlockPlaceEvent;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.SpecialBlockType;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocksManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockPlaceLis implements Listener {

    private final SpecialBlocksManager manager;
    private final NamespacedKey specialBlockTypeRetrieveKey;
    private final PluginManager pm;

    public BlockPlaceLis(JavaPlugin main, SpecialBlocksManager manager) {
        this.manager = manager;
        this.specialBlockTypeRetrieveKey = new NamespacedKey(main, "itf4if-3ffd-g3t-t3yp33e_plg445in");
        this.pm = Bukkit.getPluginManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent ev) {
        ItemStack item = ev.getItemInHand();
        Object get = GUIUtils.retrieveMark(ev.getItemInHand(), specialBlockTypeRetrieveKey, PersistentDataType.STRING);

        if(!(get instanceof String possibleValidType))
            return;

        SpecialBlockType specialBlockType = SpecialBlockType.getSpecialBlockType(possibleValidType);

        if(specialBlockType == null)
            return;

        Player player = ev.getPlayer();
        Location location = ev.getBlock().getLocation();

        boolean remove;
        if(specialBlockType == SpecialBlockType.LOADER && ((remove = manager.isLoaderItemExpired(player, item)) || manager.isChunkLoaded(location))){
            ev.setCancelled(true);
            if(remove)
                player.getInventory().removeItem(item);
            return;
        }

        SpecialBlockPlaceEvent blockPlaceEvent = new SpecialBlockPlaceEvent(player, specialBlockType, location);
        pm.callEvent(blockPlaceEvent);

        if(blockPlaceEvent.isCancelled()){
            ev.setCancelled(true);
            return;
        }

        switch (specialBlockType) {
            case LOADER:
                manager.placeBlockLoader(location, item, player);
            case COLLECTOR:
                //smth
        }
    }

}
