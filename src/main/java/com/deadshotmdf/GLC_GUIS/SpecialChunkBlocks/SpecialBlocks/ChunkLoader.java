package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.FakePlayer.FakePlayer;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Misc.NPCHelper;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public class ChunkLoader implements SpecialChunkBlock{

    private final UUID owner;
    private final long cooldown;
    private final Hologram hologram;
    private final FakePlayer npc;
    private final Location location;

    public ChunkLoader(UUID owner, JavaPlugin main, long cooldown, Location location) {
        this.owner = owner;
        this.cooldown = cooldown;
        this.hologram = DHAPI.createHologram(UUID.randomUUID() + "_" + UUID.randomUUID(), location.clone().add(0.5, 2.1, 0.5));
        this.npc = NPCHelper.createFakePlayer(main, location);

        this.location = location;
        Chunk chunk = location.getChunk();
        chunk.setForceLoaded(true);
        chunk.addPluginChunkTicket(main);
        this.updateHologram();
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    public long getRawCooldown(){
        return cooldown;
    }

    public long getCooldown(){
        return cooldown - System.currentTimeMillis();
    }

    public int getCooldownInSeconds(){
        return (int)(getCooldown() / 1000);
    }

    public boolean isExpired(){
        return getCooldownInSeconds() <= 0;
    }

    public Location getLocation(){
        return location;
    }

    public void updateHologram(){
        if(hologram == null)
            return;

        List<String> lines = ConfigSettings.getLoaderHologramLines(getCooldownInSeconds());
        HologramPage page = hologram.getPage(0);

        if(page.getLines().size() < lines.size()){
            lines.forEach(line -> DHAPI.addHologramLine(hologram, line));
            return;
        }

        for(int i = 0; i < lines.size(); i++)
            DHAPI.setHologramLine(hologram, i, lines.get(i));
    }

    @Override
    public void removeBlock(JavaPlugin main, boolean removeBlock){
        if(removeBlock)
            location.getBlock().setType(Material.AIR);
        killFakeNPC(main);
        try{hologram.destroy();}
        catch(Exception igored){}
        Chunk chunk = location.getChunk();
        chunk.setForceLoaded(false);
        chunk.removePluginChunkTicket(main);
    }

    public void killFakeNPC(JavaPlugin main){
        try {NPCHelper.removeFakePlayer(main, npc);}
        catch (Throwable ignore){}
    }

    @Override
    public SpecialBlockType getType() {
        return SpecialBlockType.LOADER;
    }
}
