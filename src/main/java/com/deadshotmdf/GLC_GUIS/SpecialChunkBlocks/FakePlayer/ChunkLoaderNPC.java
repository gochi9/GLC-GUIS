package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.FakePlayer;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface ChunkLoaderNPC {

    UUID getUniqueId();
    void die();
    Location getLocation();
    Player getPlayer();

}
