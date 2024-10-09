package com.deadshotmdf.GLC_GUIS;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ConfigSettings {

    private static String reloadConfig;
    private static String noPermission;

    private static String noAvailableCommands;
    private static String invalidCommand;
    private static String playersOnly;
    private static String consoleOnly;

    private static String genericShopDisplayItemShowAmount;
    private static String genericShopBuyLore;
    private static String genericShopSellLore;

    //Black market
    private static int blackMarketSpawnChance;
    private static long blackMarketStayDuration;
    private static long blackMarketCooldown;
    private static double scammedMinPercent;
    private static double scammedMaxPercent;
    private static double percentPriceDropPerItemSold;

    private static String blackMarketSpawnedTitle;
    private static String blackMarketSpawned;
    private static String blackMarketLeave;
    private static String npcName;
    private static String npcSkin;

    private static String blackMarketStatusHelpMessage;
    private static String blackMarketOpen;
    private static String blackMarketClosed;
    private static String blackMarketStatus;
    private static String soldItemBlackmarket;
    private static String blackMarketItemName;
    private static String blackMarketLore;
    private static String openBlackmarketGUIHelpMessage;
    private static String noBlackmarket;
    private static String warnPlayer;
    private static String robbed;
    private static String scammed;
    private static String mugged;

    //AH
    private static long ahItemExpireTime;

    private static String itemExpired;
    private static String itemBought;
    private static String boughtItem;
    private static String notEnoughFunds;
    private static String transactionNoLonger;
    private static List<String> ahListingItemLore;
    private static List<String> ahListingItemBuyer;
    private static List<String> ahListingItemPublisher;

    private static final HashMap<String, String> extraMessages = new HashMap<>();

    public static String getReloadConfig(){
        return reloadConfig;
    }

    public static String getNoPermission(){
        return noPermission;
    }

    public static String getNoAvailableCommands() {
        return noAvailableCommands;
    }

    public static String getInvalidCommand() {
        return invalidCommand;
    }

    public static String getPlayersOnly() {
        return playersOnly;
    }

    public static String getConsoleOnly() {
        return consoleOnly;
    }

    public static String getGenericShopDisplayItemShowAmount(int amount){
        return genericShopDisplayItemShowAmount.replace("{amount}", String.valueOf(amount));
    }

    public static String getGenericShopBuyLore(double amount){
        return genericShopBuyLore.replace("{amount}", s(amount));
    }

    public static String getGenericShopSellLore(double amount){
        return genericShopSellLore.replace("{amount}", s(amount));
    }

    //Black market
    public static int getBlackMarketSpawnChance(){
        return blackMarketSpawnChance;
    }

    public static long getBlackMarketStayDuration(){
        return blackMarketStayDuration;
    }

    public static long getBlackMarketCooldown(){
        return blackMarketCooldown;
    }

    public static double getScammedMinPercent(){
        return scammedMinPercent;
    }

    public static double getScammedMaxPercent(){
        return scammedMaxPercent;
    }

    public static double getPercentPriceDropPerItemSold(){
        return percentPriceDropPerItemSold;
    }

    public static String getBlackMarketStatusHelpMessage(){
        return blackMarketStatusHelpMessage;
    }

    public static String getBlackMarketOpen(){
        return blackMarketOpen;
    }

    public static String getBlackMarketClosed(){
        return blackMarketClosed;
    }

    public static String getBlackMarketStatus(boolean open){
        return blackMarketStatus.replace("{status}", open ? getBlackMarketOpen() : getBlackMarketClosed());
    }

    public static String getBlackMarketSpawnedTitle(){
        return blackMarketSpawnedTitle;
    }

    public static String getBlackMarketSpawned(long minutes){
        return blackMarketSpawned.replace("{minutes}", s(minutes));
    }

    public static String getBlackMarketLeave(){
        return blackMarketLeave;
    }

    public static String getNpcName(){
        return npcName;
    }

    public static String getNpcSkin(){
        return npcSkin;
    }

    public static String getSoldItemBlackmarket(int amount, double value){
        return soldItemBlackmarket.replace("{amount}", s(amount)).replace("{value}", GUIUtils.getDigits(value));
    }

    public static String getBlackMarketItemName(String name){
        return blackMarketItemName.replace("{name}", name);
    }

    public static String getBlackMarketLore(double value){
        return blackMarketLore.replace("{value}", GUIUtils.getDigits(value));
    }

    public static String getOpenBlackmarketGUIHelpMessage(){
        return openBlackmarketGUIHelpMessage;
    }

    public static String getNoBlackmarket(){
        return noBlackmarket;
    }

    public static String getWarnPlayer(){
        return warnPlayer;
    }

    public static String getRobbed(){
        return robbed;
    }

    public static String getScammed(double percent, double value){
        return scammed.replace("{percent}", GUIUtils.getDigits(percent)).replace("{value}", GUIUtils.getDigits(value));
    }

    public static String getMugged(){
        return mugged;
    }

    //AH
    public static long getAhItemExpireTime(){
        return ahItemExpireTime;
    }

    public static String getItemExpired(){
        return itemExpired;
    }

    public static String getItemBought(double value, double glcoins){
        return itemBought.replace("{value}", GUIUtils.getDigits(value)).replace("{glcoins}", GUIUtils.getDigits(glcoins));
    }

    public static String getBoughtItem(double value, double glcoins){
        return boughtItem.replace("{value}", GUIUtils.getDigits(value)).replace("{glcoins}", GUIUtils.getDigits(glcoins));
    }

    public static String getNotEnoughFunds(double needed, double balance){
        return notEnoughFunds.replace("{needed}", GUIUtils.getDigits(needed)).replace("{balance}", GUIUtils.getDigits(balance));
    }

    public static String getTransactionNoLonger(){
        return transactionNoLonger;
    }

    public static List<String> getAhListingItemLore(){
        return ahListingItemLore;
    }

    public static List<String> getAhListingItemBuyer(){
        return ahListingItemBuyer;
    }

    public static List<String> getAhListingItemPublisher(){
        return ahListingItemPublisher;
    }

    //

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

        reloadConfig = color(config.getString("reloadConfig"));
        noPermission = color(config.getString("noPermission"));

        //

        noAvailableCommands = color(config.getString("noAvailableCommands"));
        invalidCommand = color(config.getString("invalidCommand"));
        playersOnly = color(config.getString("playersOnly"));
        consoleOnly = color(config.getString("consoleOnly"));

        genericShopDisplayItemShowAmount = color(config.getString("genericShopDisplayItemShowAmount"));
        genericShopBuyLore = color(config.getString("genericShopBuyLore"));
        genericShopSellLore = color(config.getString("genericShopSellLore"));

        //Black market
        blackMarketSpawnChance = config.getInt("blackMarketSpawnChance");
        blackMarketStayDuration = TimeUnit.MINUTES.toMillis(config.getInt("blackMarketStayDuration"));
        blackMarketCooldown = TimeUnit.MINUTES.toMillis(config.getInt("blackMarketCooldown"));
        scammedMinPercent = config.getDouble("scammedMinPercent");
        scammedMaxPercent = config.getDouble("scammedMaxPercent");
        percentPriceDropPerItemSold = config.getDouble("percentPriceDropPerItemSold");

        blackMarketStatusHelpMessage = color(config.getString("blackMarketStatusHelpMessage"));
        blackMarketOpen = color(config.getString("blackMarketOpen"));
        blackMarketClosed = color(config.getString("blackMarketClosed"));
        blackMarketStatus = color(config.getString("blackMarketStatus"));
        blackMarketSpawnedTitle = color(config.getString("blackMarketSpawnedTitle"));
        blackMarketSpawned = color(config.getString("blackMarketSpawned"));
        blackMarketLeave = color(config.getString("blackMarketLeave"));
        npcName = color(config.getString("npcName"));
        npcSkin = color(config.getString("npcSkin"));
        soldItemBlackmarket = color(config.getString("soldItemBlackmarket"));
        blackMarketItemName = color(config.getString("blackMarketItemName"));
        blackMarketLore = color(config.getString("blackMarketLore"));
        openBlackmarketGUIHelpMessage = color(config.getString("openBlackmarketGUIHelpMessage"));
        noBlackmarket = color(config.getString("noBlackmarket"));
        warnPlayer = color(config.getString("warnPlayer"));
        robbed = color(config.getString("robbed"));
        scammed = color(config.getString("scammed"));
        mugged = color(config.getString("mugged"));

        //AH
        long expire = config.getLong("ahItemExpireTime");
        ahItemExpireTime = TimeUnit.MINUTES.toMillis(Math.max(1, expire));
        itemExpired = color(config.getString("itemExpired"));
        itemBought = color(config.getString("itemBought"));
        boughtItem = color(config.getString("boughtItem"));
        notEnoughFunds = color(config.getString("notEnoughFunds"));
        transactionNoLonger = color(config.getString("transactionNoLonger"));
        ahListingItemLore = color(config.getStringList("ahListingItemLore"));
        ahListingItemBuyer = color(config.getStringList("ahListingItemBuyer"));
        ahListingItemPublisher = color(config.getStringList("ahListingItemPublisher"));

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
        return IridiumColorAPI.process(ChatColor.translateAlternateColorCodes('&', s));
    }

    public static List<String> color(List<String> list) {
        return list == null || list.isEmpty() ? new ArrayList<>() : list.stream().map(ConfigSettings::color).collect(Collectors.toList());
    }

    private static String s(Object o){
        return String.valueOf(o);
    }

}
