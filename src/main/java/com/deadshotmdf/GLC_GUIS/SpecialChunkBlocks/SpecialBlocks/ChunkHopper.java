package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks;

import java.util.UUID;

public class ChunkHopper implements SpecialChunkBlock{

    private final UUID owner;

    public ChunkHopper(UUID owner) {
        this.owner = owner;
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public SpecialBlockType getType() {
        return SpecialBlockType.COLLECTOR;
    }

    @Override
    public void updateHologram() {

    }
}
