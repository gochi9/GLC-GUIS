package com.deadshotmdf.GLC_GUIS;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ConfigSettings {

    private static final HashMap<String, String> extraMessages = new HashMap<>();

    public static String getExtraMessage(String key){
        return key != null ? extraMessages.get(key.toLowerCase()) : null;
    }

    public static void sendExtraMessage(Player player, String key) {
        String msg = getExtraMessage(key);

        if(msg != null)
            player.sendMessage(msg);
    }

    public static void reloadConfig(GLCGGUIS main){
        main.saveDefaultConfig();
        main.reloadConfig();

        FileConfiguration config = main.getConfig();

        //

        extraMessages.clear();

        ConfigurationSection section = config.getConfigurationSection("settings");

        if(section == null)
            return;

        for(String key : section.getKeys(false)){
            String msg = section.getString(key);

            if(msg != null)
                extraMessages.put(key.toLowerCase(), color(msg));
        }
    }

    public static String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
