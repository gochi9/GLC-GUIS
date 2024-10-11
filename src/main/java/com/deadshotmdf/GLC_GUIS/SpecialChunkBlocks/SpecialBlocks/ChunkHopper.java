package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks;

import org.bukkit.Material;

import java.util.EnumMap;
import java.util.UUID;

public class ChunkHopper implements SpecialChunkBlock{

    private final UUID owner;
    private final EnumMap<Material, Integer> values;

    public ChunkHopper(UUID owner, EnumMap<Material, Integer> values) {
        this.owner = owner;
        this.values = values;
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

    public EnumMap<Material, Integer> getValues() {
        return values;
    }
}
