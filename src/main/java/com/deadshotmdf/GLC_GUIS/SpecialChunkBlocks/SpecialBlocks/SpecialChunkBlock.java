package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public interface SpecialChunkBlock {

    UUID getOwner();
    SpecialBlockType getType();
    void updateHologram();
    void removeBlock(JavaPlugin plugin, boolean isRemoveBlock);

}
