package com.deadshotmdf.GLC_GUIS.BlackMarket.Managers;

import com.deadshotmdf.GLC_GUIS.BlackMarket.Objects.BlackMarketGUI;
import com.deadshotmdf.GLC_GUIS.BlackMarket.Objects.BlackmarketEvent;
import com.deadshotmdf.GLC_GUIS.BlackMarket.Objects.BlackmarketPair;
import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.Generic.Label;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class BlackMarketManager extends AbstractGUIManager {

    private final Map<String, Location> npcSpawnPoints;
    private final List<BlackmarketPair> availableLootTable;
    private final TreeMap<Integer, BlackmarketEvent> blackmarketStackEvents, blackmarketWholeInvEvents;
    private int blackmarketStackEventsTotal, blackmarketWholeInvEventsTotal;
    private final Random random;
    private BlackMarketTimer blackMarketTimer;
    private BlackMarketGUI gui;
    private boolean blackMarketOngoing;
    private NPC npc;

    public BlackMarketManager(GuiManager guiManager, JavaPlugin plugin) {
        super(guiManager, plugin, new File(plugin.getDataFolder(), "guis/blackmarket/"), new File(plugin.getDataFolder(), "data/blackmarket.yml"));
        this.npcSpawnPoints = new HashMap<>();
        this.availableLootTable = new LinkedList<>();
        this.blackmarketStackEvents = new TreeMap<>();
        this.blackmarketWholeInvEvents = new TreeMap<>();
        this.random = new Random();
        loadInformation();
    }

    public boolean isBlackMarketOngoing() {
        return blackMarketOngoing;
    }

    public void spawnBlackMarket(){
        if(npcSpawnPoints.isEmpty())
            return;

        long minutes = TimeUnit.MILLISECONDS.toMinutes(ConfigSettings.getBlackMarketStayDuration());
        Bukkit.broadcastMessage(ConfigSettings.getBlackMarketSpawned(minutes));
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ConfigSettings.getBlackMarketSpawnedTitle(), "", 5, 20, 5);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, -0.8f);
        }

        Collections.shuffle(availableLootTable);
        this.blackMarketOngoing = true;
        gui.startBlackMarket(availableLootTable);
        Location location = GUIUtils.getRandomValue(npcSpawnPoints);
        this.npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, ConfigSettings.getNpcName());
        this.npc.getOrAddTrait(SkinTrait.class).setSkinName(ConfigSettings.getNpcSkin(), true);
        this.npc.spawn(location);
    }

    public void removeBlackMarket(){
        this.blackMarketOngoing = false;

        if(this.npc != null)
            this.npc.destroy();

        if(gui != null)
            gui.stopBlackMarket();

        Bukkit.broadcastMessage(ConfigSettings.getBlackMarketLeave());
    }

    public void openGUI(Player player){
        if(blackMarketOngoing)
            guiManager.commenceOpen(player, gui, null);
    }

    public BlackmarketEvent isEvent(boolean stack){
        BlackmarketEvent ev = randomBlackmarketEvent(-1, stack);

        return ev == null ? BlackmarketEvent.NOTHING : ev;
    }

    @Override
    protected GuiElement enhanceGuiElement(String specialType, ItemStack item, Map<String, String> extraValues, GuiElement element, String action, String[] args) {

        if(specialType == null || !specialType.equalsIgnoreCase("blackmarket") || !action.equalsIgnoreCase("BLACK_MARKET_BUY_ITEM"))
            return element;

        Double min_price = GUIUtils.getDouble(extraValues.get("min_price"));
        Double max_price = GUIUtils.getDouble(extraValues.get("max_price"));

        if(min_price == null || max_price == null)
            return new Label(item, this, guiManager, args, extraValues);
        availableLootTable.add(new BlackmarketPair(item, Math.min(min_price, max_price), Math.max(min_price, max_price)));
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
            this.gui.stopBlackMarket();

        this.blackMarketOngoing = false;
        removeBlackMarket();
        this.blackMarketTimer = new BlackMarketTimer(this);
        this.availableLootTable.clear();
        this.blackMarketTimer.runTaskTimer(plugin, 100L, 1200L);

        this.blackmarketStackEvents.clear();
        this.blackmarketWholeInvEvents.clear();
        this.blackmarketStackEventsTotal = 0;
        this.blackmarketWholeInvEventsTotal = 0;

        FileConfiguration config = plugin.getConfig();
        loadEvents(config.getConfigurationSection("eventChancesStack"), true);
        loadEvents(config.getConfigurationSection("eventChancesWholeInv"), false);
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

    public Map<String, Location> getNPCSpawnPoints(){
        return npcSpawnPoints;
    }

    private BlackmarketEvent randomBlackmarketEvent(int retries, boolean stack){
        Map.Entry<Integer, BlackmarketEvent> entry = (stack ? blackmarketStackEvents : blackmarketWholeInvEvents).ceilingEntry(random.nextInt((stack ? blackmarketStackEventsTotal : blackmarketWholeInvEventsTotal) + 1));
        return entry == null ? retries > 9 ? null : randomBlackmarketEvent(++retries, stack) : entry.getValue();
    }

    private void loadEvents(ConfigurationSection sec, boolean stack){
        if(sec == null)
            return;

        Map<Integer, BlackmarketEvent> eventMap = stack ? blackmarketStackEvents : blackmarketWholeInvEvents;
        for(String key : sec.getKeys(false)){
            BlackmarketEvent ev = BlackmarketEvent.fromString(key);
            int value = sec.getInt(key);

            if(ev == null || value < 1 || eventMap.containsValue(ev))
                continue;

            eventMap.put((stack ? (blackmarketStackEventsTotal += value) : (blackmarketWholeInvEventsTotal += value)), ev);
        }
    }

}
