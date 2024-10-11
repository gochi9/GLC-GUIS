package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Reflection;

import org.bukkit.Bukkit;

class ServerVersion
{
    private static final String version;

    private static String getNMSVersion() {
        final String[] bukkitPackage = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
        if (bukkitPackage.length <= 3) {
            return null;
        }
        return bukkitPackage[3];
    }

    public static String getVersion() {
        if (ServerVersion.version == null) {
            throw new RuntimeException("Cannot determine Bukkit version");
        }
        return ServerVersion.version;
    }

    private ServerVersion() {
    }

    static {
        version = getNMSVersion();
    }
}