package com.deadshotmdf.GLC_GUIS.AH;

import com.deadshotmdf.GLC_GUIS.AH.Objects.AHMainGUI;
import com.deadshotmdf.GLC_GUIS.AH.Objects.AHPlayerStashGUI;
import com.deadshotmdf.GLC_GUIS.AH.Objects.AhSharedGUI;
import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GLCGGUIS;
import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.gLCoins_Server.EconomyWrapper;
import com.deadshotmdf.gLCoins_Server.GLCoinsS;
import com.deadshotmdf.glccoinscommon.ModifyType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AHManager extends AbstractGUIManager {

    private final Map<UUID, AHTransaction> transactions;
    private final Map<UUID, Integer> listingsCache;
    //Expired or bought items
    private final Map<UUID, List<AHTransaction>> playerItems;
    private GUI gui, player_stash;

    public AHManager(GuiManager guiManager, JavaPlugin plugin) {
        super(guiManager, plugin, new File(plugin.getDataFolder(), "guis/ah/"), new File(plugin.getDataFolder(), "data/ah.yml"));
        this.transactions = new LinkedHashMap<>();
        this.listingsCache = new HashMap<>();
        this.playerItems = new HashMap<>();
        loadInformation();
        AHExpireTimer timer = new AHExpireTimer(this, transactions);
        timer.runTaskTimer(plugin, 40, 1200);
    }

    public void openGUI(HumanEntity player, boolean main){
        guiManager.commenceOpen(player, main ? gui : player_stash, null);
    }

    //This method assumes that the sell_amount or/and glcoins values are > 0.000 and the item in main hand is valid
    public void addItem(Player player, ItemStack item, double sell_amount, double glcoins){
        UUID id = GUIUtils.getUniqueID(transactions);
        AHTransaction transaction = new AHTransaction(id, player.getUniqueId(), item, System.currentTimeMillis() + ConfigSettings.getAhItemExpireTime(), sell_amount, glcoins);
        transactions.put(id, transaction);
        player.getInventory().setItemInMainHand(null);
        onAHUpdate();
        player.sendMessage(ConfigSettings.getAHSellListed());
    }

    public void itemRemoveExpire(AHTransaction transaction, boolean expire){
        if(transaction != null)
            itemRemoveExpireBuy(transaction, null, 0, 0, expire);
    }

    public void itemBuy(AHTransaction transaction, Player buyer, double sell_amount, double glcoins){
        if(transaction != null && buyer != null)
            itemRemoveExpireBuy(transaction, buyer, sell_amount, glcoins, false);
    }

    private void itemRemoveExpireBuy(AHTransaction transaction, Player buyer, double sell_amount, double glcoins, boolean onExpire){
        if(!transaction.isStillValid())
            return;

        boolean buy = buyer != null;
        UUID publisher = transaction.getPublisher();
        Player seller = Bukkit.getPlayer(publisher);

        if(buy && buyer.isOnline())
            buyer.sendMessage(ConfigSettings.getBoughtItem(sell_amount, glcoins));

        if(seller != null && seller.isOnline() && (buy || onExpire))
            seller.sendMessage(buy ? ConfigSettings.getItemBought(sell_amount, glcoins) : ConfigSettings.getItemAHExpired());

        UUID uuid = buy ? buyer.getUniqueId() : publisher;

        EconomyWrapper economy = GLCGGUIS.getEconomy();
        if(buy){
            if(sell_amount > 0.000){
                economy.withdrawPlayer(buyer, sell_amount);
                economy.depositPlayer(Bukkit.getOfflinePlayer(publisher), sell_amount);
            }
            if(glcoins > 0.000){
                GLCoinsS.getDatabase().modifyEntryAsync(buyer.getUniqueId(), glcoins, ModifyType.REMOVE, null);
                GLCoinsS.getDatabase().modifyEntryAsync(publisher, glcoins, ModifyType.ADD, null);
            }
        }

        transactions.remove(transaction.getTransactionID());
        transaction.setNoLongerValid();
        playerItems.computeIfAbsent(uuid, _ -> new LinkedList<>()).add(new AHTransaction(uuid, transaction.getItem()));
        onAHUpdate();
    }

    public void onAHUpdate(){
        this.listingsCache.clear();
        guiManager.refreshInventories(AhSharedGUI.class);
    };

    public AHTransaction getTransaction(UUID uuid) {
        return transactions.get(uuid != null ? uuid : GUIUtils.getUniqueID(transactions));
    }

    public int getListingsPlayer(UUID uuid){
        Integer listings = listingsCache.get(uuid);

        if(listings != null)
            return listings;

        listings = 0;
        for(AHTransaction transaction : new LinkedList<>(transactions.values())){
            if(transaction != null && transaction.getPublisher().equals(uuid))
                ++listings;
        }

        return listings;
    }

    public Map<UUID, AHTransaction> getTransactions() {
        return new LinkedHashMap<>(transactions);
    }

    public Collection<AHTransaction> getPlayerStash(UUID uuid) {
        return new LinkedList<>(playerItems.computeIfAbsent(uuid, _ -> new LinkedList<>()));
    }

    @Override
    protected GUI specifyGUI(boolean perPlayer, GuiManager guiManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> mergedPages, String type){
        return switch (type != null ? type.toUpperCase() : "null") {
            case "AH" -> (gui = new AHMainGUI(guiManager, this, title, size, mergedPages, null, null));
            case "AH_PLAYER_STASH" -> (player_stash = new AHPlayerStashGUI(guiManager, this, title, size, mergedPages, null, null));
            default -> super.specifyGUI(perPlayer, guiManager, title, size, mergedPages, type);
        };
    }

    @Override
    public void saveInformation(){
        this.config.set("ah", null);
        this.config.set("player_stash", null);

        new LinkedHashMap<>(transactions).values().forEach(transaction -> {
            if(transaction == null || !transaction.isStillValid() || !transaction.doesExist())
                return;

            String path = "ah." + transaction.getTransactionID().toString() + ".";
            config.set(path + ".publisher", transaction.getPublisher().toString());
            config.set(path + ".expire", transaction.getExpire());
            config.set(path + ".price", transaction.getSellAmount());
            config.set(path + ".glcoins", transaction.getGLCoinsSellAmount());
            config.set(path + ".item", transaction.getItem());
        });

        new HashMap<>(playerItems).forEach((uuid, list) ->{
            if(uuid == null || list == null)
                return;

            AtomicInteger delimiter = new AtomicInteger(0);
            new LinkedList<>(list).forEach(transaction -> {
                if(transaction != null && transaction.doesExist())
                    config.set("player_stash." + uuid + "." + delimiter.getAndIncrement(), transaction.getItem());
            });
        });

        this.saveC();
    }

    @Override
    public void loadInformation() {
        transactions.clear();
        playerItems.clear();

        loadTransactions();
        loadPlayerStash();
    }

    private void loadTransactions() {
        getKeys("ah", false).forEach(idStr -> {
            UUID transactionID = GUIUtils.getUUID(idStr);

            if (transactionID == null)
                return;

            String path = "ah." + idStr + ".";
            UUID publisher = GUIUtils.getUUID(config.getString(path + "publisher", ""));

            if(publisher == null)
                return;

            long expire = config.getLong(path + "expire");
            double price = config.getDouble(path + "price");
            double glcoins = config.getDouble(path + "glcoins");
            ItemStack item = config.getItemStack(path + "item");

            if (item != null)
                transactions.put(transactionID, new AHTransaction(
                    transactionID, publisher, item, expire, price, glcoins));
        });
    }

    private void loadPlayerStash() {
        getKeys("player_stash", false).forEach(uuidStr -> {
            UUID uuid = GUIUtils.getUUID(uuidStr);
            if (uuid == null)
                return;

            List<AHTransaction> list = new LinkedList<>();
            String path = "player_stash." + uuidStr;

            ConfigurationSection sec = config.getConfigurationSection(path);

            if(sec == null)
                return;

            sec.getKeys(false).forEach(indexStr -> {
                ItemStack item = config.getItemStack(path + "." + indexStr);
                if (item != null)
                    list.add(new AHTransaction(uuid, item));
            });

            if (!list.isEmpty())
                playerItems.put(uuid, list);
        });
    }
}
