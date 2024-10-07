package com.deadshotmdf.GLC_GUIS;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigSettings {

    private static String genericShopDisplayItemShowAmount;
    private static String genericShopBuyLore;
    private static String genericShopSellLore;

    private static final HashMap<String, String> extraMessages = new HashMap<>();

    public static String getGenericShopDisplayItemShowAmount(int amount){
        return genericShopDisplayItemShowAmount.replace("{amount}", String.valueOf(amount));
    }

    public static String getGenericShopBuyLore(double amount){
        return genericShopBuyLore.replace("{amount}", s(amount));
    }

    public static String getGenericShopSellLore(double amount){
        return genericShopSellLore.replace("{amount}", s(amount));
    }

    public static String getExtraMessage(String key){
        return key != null ? extraMessages.get(key.toLowerCase()) : null;
    }

    public static void sendExtraMessage(HumanEntity player, String key) {
        String msg = getExtraMessage(key);

        if(msg != null)
            player.sendMessage(msg);
    }

    public static void reloadConfig(GLCGGUIS main){
        main.saveDefaultConfig();
        main.reloadConfig();

        FileConfiguration config = main.getConfig();

        //

        genericShopDisplayItemShowAmount = color(config.getString("genericShopDisplayItemShowAmount"));
        genericShopBuyLore = color(config.getString("genericShopBuyLore"));
        genericShopSellLore = color(config.getString("genericShopSellLore"));

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

    public static List<String> color(List<String> list) {
        return list == null || list.isEmpty() ? new ArrayList<>() : list.stream().map(ConfigSettings::color).collect(Collectors.toList());
    }

    private static String s(Object o){
        return String.valueOf(o);
    }

}
