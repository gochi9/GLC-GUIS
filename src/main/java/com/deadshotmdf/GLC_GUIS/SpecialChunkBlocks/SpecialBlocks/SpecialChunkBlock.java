package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks;

import java.util.UUID;

public interface SpecialChunkBlock {

    UUID getOwner();
    SpecialBlockType getType();
    void updateHologram();

}
