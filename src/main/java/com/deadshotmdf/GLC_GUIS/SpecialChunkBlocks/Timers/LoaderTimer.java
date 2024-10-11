package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Timers;

import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.ChunkLoader;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.SpecialChunkBlock;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocksManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Map;

public class LoaderTimer implements Runnable{

    private final JavaPlugin main;
    private final SpecialBlocksManager manager;
    private final Map<Location, SpecialChunkBlock> blocks;
    private final BukkitScheduler scheduler;

    public LoaderTimer(JavaPlugin main, SpecialBlocksManager manager, Map<Location, SpecialChunkBlock> blocks) {
        this.main = main;
        this.manager = manager;
        this.blocks = blocks;
        this.scheduler = Bukkit.getScheduler();
    }

    public void run() {
        for(Map.Entry<Location, SpecialChunkBlock> entry : blocks.entrySet()){
            SpecialChunkBlock block = entry.getValue();
            if(!(block instanceof ChunkLoader loader))
                continue;

            if(loader.isExpired())
                scheduler.runTask(main, () -> manager.removeLoader(entry.getKey(), loader));
            else
                block.updateHologram();
        }
    }

}
