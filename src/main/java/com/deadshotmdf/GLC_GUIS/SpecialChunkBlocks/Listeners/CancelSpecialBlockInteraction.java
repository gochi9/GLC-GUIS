package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Listeners;

import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocksManager;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;

public class CancelSpecialBlockInteraction implements Listener {

    private final SpecialBlocksManager specialBlocksManager;

    public CancelSpecialBlockInteraction(SpecialBlocksManager specialBlocksManager) {
        this.specialBlocksManager = specialBlocksManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent ev) {
        ev.setCancelled(specialBlocksManager.isSpecialBlock(ev.getBlock().getLocation()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent ev) {
        ev.setCancelled(specialBlocksManager.isSpecialBlock(ev.getBlock().getLocation()) || isBlock(ev.blockList()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent ev) {
        ev.setCancelled(isBlock(ev.blockList()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent ev) {
        ev.setCancelled(isBlock(ev.getBlocks()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent ev) {
        ev.setCancelled(isBlock(ev.getBlocks()));
    }

    private boolean isBlock(List<Block> blocks){
        for(Block block : blocks)
            if(specialBlocksManager.isSpecialBlock(block.getLocation()))
                return true;

        return false;
    }

}
