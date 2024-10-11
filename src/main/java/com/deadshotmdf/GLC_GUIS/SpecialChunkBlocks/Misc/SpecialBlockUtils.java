package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Misc;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SpecialBlockUtils {

    public static Long parseDuration(String input) {
        if (input == null || input.isEmpty())
            return null;

        String pattern = "^\\d+[smhdSMHD]$";
        if (!input.matches(pattern))
            return null;

        char unit = Character.toLowerCase(input.charAt(input.length() - 1));
        String numberPart = input.substring(0, input.length() - 1);

        long number;
        try {number = Long.parseLong(numberPart);}
        catch (NumberFormatException e) {return null;}

        long milliseconds;
        switch (unit) {
            case 's':
                milliseconds = number * 1000L;
                break;
            case 'm':
                milliseconds = number * 60L * 1000L;
                break;
            case 'h':
                milliseconds = number * 60L * 60L * 1000L;
                break;
            case 'd':
                milliseconds = number * 24L * 60L * 60L * 1000L;
                break;
            default:
                return null;
        }

        long currentTime = System.currentTimeMillis();
        if (Long.MAX_VALUE - currentTime < milliseconds)
            return null;

        return currentTime + milliseconds;
    }

    public static UUID getUUID(String s){
        try{
            return UUID.fromString(s);
        }
        catch (Throwable ignored){
            return null;
        }
    }

    public static void showSurroundingChunkParticles(Player player, Location loc, Particle particle) {
        int chunkX = loc.getBlockX() >> 4;
        int chunkZ = loc.getBlockZ() >> 4;

        double minX = ((chunkX - 1) << 4);
        double maxX = ((chunkX + 2) << 4);
        double minZ = ((chunkZ - 1) << 4);
        double maxZ = ((chunkZ + 2) << 4);

        double minY = loc.getY() - 16;
        double maxY = loc.getY() + 16;

        World world = loc.getWorld();
        fillWall(player, particle, world, minX, minY, maxY, minZ, maxZ, true);
        fillWall(player, particle, world, maxX, minY, maxY, minZ, maxZ, true);
        fillWall(player, particle, world, minZ, minY, maxY, minX, maxX, false);
        fillWall(player, particle, world, maxZ, minY, maxY, minX, maxX, false);
    }

    private static void fillWall(Player player, Particle particle, World world, double fixedCoord, double minY, double maxY, double minVar, double maxVar, boolean isXFixed) {
        for (double y = minY; y <= maxY; y++)
        for (double var = minVar; var <= maxVar; var++)
            player.spawnParticle(particle, isXFixed ? new Location(world, fixedCoord, y, var) : new Location(world, var, y, fixedCoord), 0, 0, 0, 0, 0);
    }

}
