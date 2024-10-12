package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks;

import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;
import java.util.UUID;

public class ChunkHopper implements SpecialChunkBlock{

    private final UUID owner;
    private final Location location;
    private final EnumMap<Material, Integer> values;
    private final Hologram hologram;
    private GUI collectorGUI;

    public ChunkHopper(UUID owner, Location location, EnumMap<Material, Integer> values) {
        this.owner = owner;
        this.location = location;
        this.values = values;
        this.hologram = DHAPI.createHologram(UUID.randomUUID() + "_" + UUID.randomUUID(), location.clone().add(0.5, 2.1, 0.5));
        updateHologram();
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public SpecialBlockType getType() {
        return SpecialBlockType.COLLECTOR;
    }

    public void trimUselessInfo(){
        values.entrySet().removeIf(entry -> entry.getValue() < 1);
    }

    public void setGUI(GUI collectorGUI){
        this.collectorGUI = collectorGUI;
    }

    public GUI getCollectorGUI(){
        return collectorGUI;
    }

    @Override
    public void updateHologram() {

    }

    @Override
    public void removeBlock(JavaPlugin plugin, boolean removeBlock){
        if(removeBlock)
            location.getBlock().setType(Material.AIR);

        try{hologram.destroy();}
        catch(Exception ignored){}
    }

    public EnumMap<Material, Integer> getValues() {
        return values;
    }
}
