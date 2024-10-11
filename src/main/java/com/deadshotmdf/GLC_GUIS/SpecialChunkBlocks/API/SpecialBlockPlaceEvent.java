package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.API;

import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.SpecialBlockType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpecialBlockPlaceEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final SpecialBlockType type;
    private final Location location;
    private boolean cancelled;

    public SpecialBlockPlaceEvent(Player player, SpecialBlockType type, Location location) {
        this.player = player;
        this.type = type;
        this.location = location;
    }

    public Player getPlayer() {
        return player;
    }

    public SpecialBlockType getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
