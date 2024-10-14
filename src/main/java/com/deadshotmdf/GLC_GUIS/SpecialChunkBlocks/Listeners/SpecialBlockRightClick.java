package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Listeners;

import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.ChunkHopper;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.ChunkLoader;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.SpecialChunkBlock;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocksManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class SpecialBlockRightClick implements Listener {

    private final SpecialBlocksManager specialBlocksManager;

    public SpecialBlockRightClick(SpecialBlocksManager specialBlocksManager) {
        this.specialBlocksManager = specialBlocksManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerRightClick(PlayerInteractEvent ev) {
        if(ev.useInteractedBlock() == Event.Result.DENY || ev.useItemInHand() == Event.Result.DENY)
            return;

        if(ev.getHand() != EquipmentSlot.HAND || ev.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Block block = ev.getClickedBlock();

        if(block == null)
            return;

        Location location = block.getLocation();
        SpecialChunkBlock specialChunkBlock = specialBlocksManager.getSpecialBlock(location);

        if(specialChunkBlock == null)
            return;

        Player player = ev.getPlayer();
        if(specialChunkBlock instanceof ChunkLoader loader)
            specialBlocksManager.onRightClickLoader(location, player, loader);
        else if(specialChunkBlock instanceof ChunkHopper collector)
            specialBlocksManager.onRightClickCollector(location, player, collector);

        ev.setCancelled(true);
        ev.setUseInteractedBlock(Event.Result.DENY);
        ev.setUseItemInHand(Event.Result.DENY);
    }

}
