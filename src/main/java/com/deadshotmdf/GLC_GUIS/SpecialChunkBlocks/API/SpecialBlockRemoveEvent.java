package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.API;

import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.SpecialBlockType;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class SpecialBlockRemoveEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID uuid;
    private final SpecialBlockType type;
    private final Location location;
    private boolean cancelled;

    public SpecialBlockRemoveEvent(UUID uuid, SpecialBlockType type, Location location) {
        this.uuid = uuid;
        this.type = type;
        this.location = location;
    }

    public UUID getUUID() {
        return uuid;
    }

    public SpecialBlockType getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
