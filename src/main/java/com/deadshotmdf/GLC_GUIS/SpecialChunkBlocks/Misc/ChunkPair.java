package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Misc;

import org.bukkit.Chunk;

import java.util.Objects;
import java.util.UUID;

public class ChunkPair {

    private final UUID world;
    private final int x;
    private final int z;

    public ChunkPair(Chunk chunk) {
        this.world = chunk.getWorld().getUID();
        this.x = chunk.getX();
        this.z = chunk.getZ();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof ChunkPair chunkPair))
            return false;

        return x == chunkPair.x && z == chunkPair.z && Objects.equals(world, chunkPair.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, z);
    }
}
