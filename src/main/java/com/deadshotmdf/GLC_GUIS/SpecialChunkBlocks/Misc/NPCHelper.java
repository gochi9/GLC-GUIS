package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Misc;

import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.FakePlayer.FakePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.UUID;

public class NPCHelper {

    private static int NPCS_COUNTER = 0;

    public static String getRandomName(String worldName){
        return "Loader-" + (worldName.length() > 7 ? worldName.substring(0, 7) : worldName) + "-" + (NPCS_COUNTER++);
    }

    private static final HashSet<UUID> uuids = new HashSet<>();

    public static UUID getUniqueUUID(){
        UUID uuid = UUID.randomUUID();

        while(uuids.contains(uuid))
            uuid = UUID.randomUUID();

        uuids.add(uuid);
        return uuid;
    }

    public static void removeUUID(UUID uuid){
        uuids.remove(uuid);
    }

    public static FakePlayer createFakePlayer(JavaPlugin main, Location location){
        FakePlayer npc = new FakePlayer(((CraftServer) Bukkit.getServer()).getServer(), location, getUniqueUUID());
        npc.getPlayer().setMetadata("NPC", new FixedMetadataValue(main, true));
        return npc;
    }

    public static void removeFakePlayer(JavaPlugin main, FakePlayer npc){
        npc.getPlayer().removeMetadata("NPC", main);
        removeUUID(npc.getUniqueId());
        npc.die();
    }

}
