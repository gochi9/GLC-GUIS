package com.deadshotmdf.GLC_GUIS;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    private static String itemAHExpired;
    private static String itemBought;
    private static String boughtItem;
    private static String notEnoughFunds;
    private static String transactionNoLonger;
    private static String ahSellHelpMessage;
    private static String ahSellSyntax;
    private static String ahSellMustHoldItem;
    private static String ahSellInvalidPries;
    private static String ahSellNoMoreSpace;
    private static String ahSellListed;
    private static String ahConfirm;
    private static String removedListing;
    private static List<String> ahListingItemLore;
    private static List<String> ahListingItemBuyer;
    private static List<String> ahListingItemPublisher;
    private static List<String> ahSortLore;

    //Special Chunk Blocks
    private static String itemExpired;
    private static String dateFormat;
    private static String timeLeftFormat;
    private static List<String> loaderHologramLines;

    private static List<String> collectorHologramLines;
    private static String chunkCollectorItemName;
    private static List<String> chunkCollectorItemLore;

    //Mayor
    private static String upgradeInProgress;

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
        return color(blackMarketItemName.replace("{name}", name));
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

    public static String getItemAHExpired(){
        return itemAHExpired;
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

    public static String getAHSellHelpMessage(){
        return ahSellHelpMessage;
    }

    public static String getAHSellSyntax(){
        return ahSellSyntax;
    }

    public static String getAHSellMustHoldItem(){
        return ahSellMustHoldItem;
    }

    public static String getAHSellInvalidPries(){
        return ahSellInvalidPries;
    }

    public static String getAHSellNoMoreSpace(){
        return ahSellNoMoreSpace;
    }

    public static String getAHSellListed(){
        return ahSellListed;
    }

    public static String getAHConfirm() {
        return ahConfirm;
    }

    public static String getRemovedListing(String player){
        return removedListing.replace("{player}", player);
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

    public static List<String> getAHSortLore(){
        return ahSortLore;
    }

    //Special Chunk Block
    public static String getItemExpired(){
        return itemExpired;
    }

    public static String getDateFormat() {
        return dateFormat;
    }

    public static String getTimeLeftFormat(int totalSeconds) {
        Duration duration = Duration.ofSeconds(totalSeconds);

        long days = duration.toDays();
        duration = duration.minusDays(days);

        long hours = duration.toHours();
        duration = duration.minusHours(hours);

        long minutes = duration.toMinutes();
        long seconds = duration.minusMinutes(minutes).getSeconds();
        return timeLeftFormat.replace("{days}", s(days)).replace("{hours}", s(hours)).replace("{minutes}", s(minutes)).replace("{seconds}", s(seconds));
    }

    private static final String[] loaderHologramPlaceholder = {"{left}"};

    public static List<String> getLoaderHologramLines(int totalSeconds) {
        return replaceList(loaderHologramLines, loaderHologramPlaceholder, getTimeLeftFormat(totalSeconds));
    }

    public static List<String> getCollectorHologramLines(){
        return collectorHologramLines;
    }

    public static String getChunkCollectorItemName(String name, int amount, int maxAmount){
        return chunkCollectorItemName.replace("{name}", name).replace("{amount}", s(amount)).replace("{maxAmount}", s(maxAmount));
    }

    private static final String[] chunkCollectorItemLorePlaceholder = {"{sell}"};

    public static List<String> getChunkCollectorItemLore(double sell){
        return replaceList(chunkCollectorItemLore, chunkCollectorItemLorePlaceholder, GUIUtils.getDigits(sell));
    }

    //Mayor
    public static String getUpgradeInProgress(long delay, double glcoins){
        return upgradeInProgress.replace("{date}", GUIUtils.convertMillisToDate(delay)).replace("{price_glcoins}", s(glcoins));
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
        blackMarketItemName = config.getString("blackMarketItemName");
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
        itemAHExpired = color(config.getString("itemAHExpired"));
        itemBought = color(config.getString("itemBought"));
        boughtItem = color(config.getString("boughtItem"));
        notEnoughFunds = color(config.getString("notEnoughFunds"));
        transactionNoLonger = color(config.getString("transactionNoLonger"));
        ahSellHelpMessage = color(config.getString("ahSellHelpMessage"));
        ahSellSyntax = color(config.getString("ahSellSyntax"));
        ahSellMustHoldItem = color(config.getString("ahSellMustHoldItem"));
        ahSellInvalidPries = color(config.getString("ahSellInvalidPries"));
        ahSellNoMoreSpace = color(config.getString("ahSellNoMoreSpace"));
        ahSellListed = color(config.getString("ahSellListed"));
        ahConfirm = color(config.getString("ahConfirm"));
        removedListing = color(config.getString("removedListing"));
        ahListingItemLore = color(config.getStringList("ahListingItemLore"));
        ahListingItemBuyer = color(config.getStringList("ahListingItemBuyer"));
        ahListingItemPublisher = color(config.getStringList("ahListingItemPublisher"));
        ahSortLore = config.getStringList("ahSortLore");

        //Special Chunk Blocks
        itemExpired = color(config.getString("itemExpired"));
        dateFormat = color(config.getString("dateFormat"));
        timeLeftFormat = color(config.getString("timeLeftFormat"));
        loaderHologramLines = config.getStringList("loaderHologramLines");

        collectorHologramLines = color(config.getStringList("collectorHologramLines"));
        chunkCollectorItemName = color(config.getString("chunkCollectorItemName"));
        chunkCollectorItemLore = color(config.getStringList("chunkCollectorItemLore"));

        //Mayor
        upgradeInProgress = color(config.getString("upgradeInProgress"));

        extraMessages.clear();

        ConfigurationSection section = config.getConfigurationSection("extraMessages");

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

    public static List<String> replaceList(List<String> list, String[] placeholder, String... replacement){
        List<String> newList = new ArrayList<>(list.size());

        for(String s : list)
            newList.add(color(StringUtils.replaceEach(s, placeholder, replacement)));

        return newList;
    }

    public static String formatDate(long milliseconds) {
        String defaultFormat = "dd/MM/yyyy-HH:mm:ss";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
            return dateTime.format(formatter);
        }
        catch (IllegalArgumentException | DateTimeParseException e) {
            System.err.println("Invalid date format: " + dateFormat + ". Using default format: " + defaultFormat);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(defaultFormat);
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
            return dateTime.format(formatter);
        }
    }

    private static String s(Object o){
        return String.valueOf(o);
    }

}
