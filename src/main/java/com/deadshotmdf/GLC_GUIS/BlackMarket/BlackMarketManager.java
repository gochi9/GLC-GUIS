package com.deadshotmdf.GLC_GUIS.BlackMarket;

import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.Generic.Label;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class BlackMarketManager extends AbstractGUIManager {

    private final Map<String, Location> npcSpawnPoints;
    private final Map<UUID, BlackmarketPair> availableLootTable;
    private final List<BlackmarketPair> currentlySpawnedLootTable;
    private BlackMarketTimer blackMarketTimer;
    private BlackMarketGUI gui;
    private boolean blackMarketOngoing;
    private NPC npc;

    public BlackMarketManager(GuiManager guiManager, JavaPlugin plugin) {
        super(guiManager, plugin, new File(plugin.getDataFolder(), "guis/blackmarket/"), new File(plugin.getDataFolder(), "data/blackmarket.yml"));
        this.npcSpawnPoints = new HashMap<>();
        this.availableLootTable = new HashMap<>();
        this.currentlySpawnedLootTable = new ArrayList<>();
        loadInformation();
    }

    public boolean isBlackMarketOngoing() {
        return blackMarketOngoing;
    }

    public void spawnBlackMarket(){
        this.blackMarketOngoing = true;
    }

    public void removeBlackMarket(){
        this.blackMarketOngoing = false;

        if(this.npc == null)
            return;

        this.currentlySpawnedLootTable.clear();
        this.npc.destroy();

        if(gui == null)
            return;

        gui.forceClose();
        gui.deletePages();
    }

    public void warnPlayer(Player player){

    }

    public void openGUI(Player player){
        guiManager.commenceOpen(player, gui, null);
    }

    @Override
    protected GuiElement enhanceGuiElement(String specialType, ItemStack item, Map<String, String> extraValues, GuiElement element, String action, String[] args) {
        if(specialType == null || !specialType.equalsIgnoreCase("blackmarket") || !action.equalsIgnoreCase("BLACK_MARKET_BUY_ITEM"))
            return element;

        Double min_price = GUIUtils.getDouble("min_price");
        Double max_price = GUIUtils.getDouble("max_price");

        if(min_price == null || max_price == null)
            return new Label(item, this, guiManager, args, extraValues);

        UUID id = GUIUtils.getUniqueID(availableLootTable);
        availableLootTable.put(id, new BlackmarketPair(item, Math.min(min_price, max_price), Math.max(min_price, max_price), id));
        return null;
    }

    @Override
    protected GUI specifyGUI(boolean perPlayer, GuiManager guiManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> mergedPages, String type){
        return switch (type != null ? type.toUpperCase() : "null") {
            case "BLACKMARKET" -> gui = new BlackMarketGUI(guiManager, this, title, size, mergedPages);
            default -> super.specifyGUI(perPlayer, guiManager, title, size, mergedPages, type);
        };
    }

    @Override
    public void onReload(){
        if(blackMarketTimer != null)
            blackMarketTimer.cancel();

        if(gui != null)
            this.gui.deletePages();

        this.blackMarketOngoing = false;
        removeBlackMarket();
        this.blackMarketTimer = new BlackMarketTimer(this);
        this.currentlySpawnedLootTable.clear();
        this.availableLootTable.clear();
        this.blackMarketTimer.runTaskTimer(plugin, 100L, 1200L);
    }

    public NPC getNpc(){
        return npc;
    }

    @Override
    public void saveInformation(){
        removeBlackMarket();
        this.config.set("locations", null);
        saveC();

        this.npcSpawnPoints.forEach((id, location) -> this.config.set("locations." + id, location));
        saveC();
    }

    @Override
    public void loadInformation(){
        Set<String> keys = getKeys("locations", false);

        for(String key : keys){
            Location location = this.config.getLocation("locations." + key);

            if(location != null)
                npcSpawnPoints.put(key.toLowerCase(), location);
        }
    }

}
