package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.SpecialChunkBlocks.GiveableSpecialChunkBlock;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.API.SpecialBlockRemoveEvent;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.GUI.LoaderGUI;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Misc.ChunkPair;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Misc.SpecialBlockUtils;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.ChunkLoader;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.SpecialBlockType;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.SpecialChunkBlock;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Timers.LoaderTimer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SpecialBlocksManager extends AbstractGUIManager {

    public final static String[] loaderPlaceholers = new String[]{"{date}"};
    private final NamespacedKey specialKey, loaderKey;
    private final Map<Location, SpecialChunkBlock> blocks;
    private final HashSet<ChunkPair> loadedChunks;
    private GiveableSpecialChunkBlock loader, collector;
    private GUI loaderGUI, collectorGUI;

    public SpecialBlocksManager(GuiManager guiManager, JavaPlugin plugin) {
        super(guiManager, plugin, new File(plugin.getDataFolder(), "guis/specialblocks/"), new File(plugin.getDataFolder(), "data/specialblocks.yml"));
        this.blocks = new ConcurrentHashMap<>();
        this.specialKey = new NamespacedKey(plugin, "itf4if-3ffd-g3t-t3yp33e_plg445in");
        this.loaderKey = new NamespacedKey(plugin, "l0ade3er-k33kyd-plgguin43jf-34rf");
        this.loadedChunks = new HashSet<>();
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new LoaderTimer(plugin, this, blocks), 20L, 20L);
        loadInformation();
    }

    public boolean isSpecialBlock(Location loc) {
        return blocks.containsKey(loc);
    }

    public SpecialChunkBlock getSpecialBlock(Location loc) {
        return blocks.get(loc);
    }

    public void addSpecialBlock(Location loc, SpecialChunkBlock block) {
        blocks.put(loc, block);
    }

    public void removeBlock(Location loc) {
        blocks.remove(loc);
    }

    protected Map<Location, SpecialChunkBlock> getBlocks() {
        return blocks;
    }

    public ItemStack getLoaderItem(long cooldown){
        return loader.getItemStackMarkedAndReplaced(loaderKey, PersistentDataType.LONG, cooldown, loaderPlaceholers, ConfigSettings.formatDate(cooldown));
    }

    public void removeLoader(Location location, ChunkLoader block) {
        block.removeChunk(plugin, true);
        removeBlock(location);
        loadedChunks.remove(new ChunkPair(location.getChunk()));
        Bukkit.getPluginManager().callEvent(new SpecialBlockRemoveEvent(block.getOwner(), SpecialBlockType.LOADER, location));
    }

    public void onRightClickLoader(Location location, Player player, ChunkLoader block) {
        if(block.getOwner().equals(player.getUniqueId()))
            guiManager.commenceOpen(player, loaderGUI, null, location, block);
    }

    public void placeBlockLoader(Location location, ItemStack item, Player owner){
        Object get = GUIUtils.retrieveMark(item, loaderKey, PersistentDataType.LONG);
        long cooldown = (get instanceof Long l) ? l : System.currentTimeMillis() + 1500;
        addSpecialBlock(location, new ChunkLoader(owner.getUniqueId(), plugin, cooldown, location));
        loadedChunks.add(new ChunkPair(location.getChunk()));
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> SpecialBlockUtils.showSurroundingChunkParticles(owner, owner.getLocation(), Particle.CHERRY_LEAVES));
    }

    public boolean isChunkLoaded(Location location){
        return loadedChunks.contains(new ChunkPair(location.getChunk()));
    }

    public void retakeLoader(Player player, Location location){
        SpecialChunkBlock block = getSpecialBlock(location);

        if(!(block instanceof ChunkLoader loader))
            return;

        long cooldown = loader.getRawCooldown();
        removeLoader(location, loader);
        UUID uuid = player.getUniqueId();
        addPlayerItem(player, cooldown, location, uuid);
    }

    public void addPlayerItem(Player player, long cooldown, Location location, UUID uuid){
        HashMap<Integer, ItemStack> remains =  player.getInventory().addItem(getLoaderItem(cooldown));

        if(!remains.isEmpty())
            remains.values().forEach(remain -> player.getWorld().dropItem(location, remain).setOwner(uuid));
    }

    public boolean isLoaderItemExpired(Player sender, ItemStack itemStack){
        Object o = GUIUtils.retrieveMark(itemStack, loaderKey, PersistentDataType.LONG);

        if(!(o instanceof Long cooldown))
            return false;

        if(cooldown - System.currentTimeMillis() <= 0){
            sender.sendMessage(ConfigSettings.getItemExpired());
            return true;
        }

        return false;
    }

    @Override
    protected GuiElement enhanceGuiElement(String specialType, ItemStack item, Map<String, String> extraValues, GuiElement element, String action, String[] args) {
        if(!action.equalsIgnoreCase("GIVE_SPECIAL_CHUNK_BLOCK"))
            return element;

        SpecialBlockType type = SpecialBlockType.getSpecialBlockType(args.length > 0 ? args[0] : "null");

        if(type == null)
            return element;

        element.addInitialMark(specialKey, PersistentDataType.STRING, type.toString());

        switch (type) {
            case LOADER:
                loader = (GiveableSpecialChunkBlock) element;
                break;
            case COLLECTOR:
                collector = (GiveableSpecialChunkBlock) element;
                break;
            default:
                return element;
        }

        return null;
    }

    @Override
    protected GUI specifyGUI(boolean perPlayer, GuiManager guiManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> mergedPages, String type){
        return switch (type != null ? type.toUpperCase() : "null") {
            case "CHUNK_LOADER" -> (loaderGUI = new LoaderGUI(guiManager, this, title, size, mergedPages, null, null));
          //  case "AH_PLAYER_STASH" -> (player_stash = new AHPlayerStashGUI(guiManager, this, title, size, mergedPages, null, null));
            default -> super.specifyGUI(perPlayer, guiManager, title, size, mergedPages, type);
        };
    }

    @Override
    public void saveInformation(){
        AtomicInteger i = new AtomicInteger(0);
        config.set("loaders", null);
        saveC();
        new HashMap<>(getBlocks()).forEach((k, v) ->{
            if(!(v instanceof ChunkLoader loader))
                return;

            String path = "loaders." + i.getAndIncrement();
            config.set(path + ".cooldown", loader.getRawCooldown());
            config.set(path + ".owner", loader.getOwner().toString());
            config.set(path + ".location", k);
            loader.removeChunk(plugin, false);
        });

        saveC();
        loadedChunks.clear();
    }

    @Override
    public void loadInformation(){
        long current = System.currentTimeMillis();
        for(String key : getKeys("loaders", false)){
            String path = "loaders." + key;
            long cooldown = config.getLong(path + ".cooldown");
            Location location = config.getLocation(path + ".location");
            UUID owner = SpecialBlockUtils.getUUID(config.getString(path + ".owner"));

            if(cooldown <= 0 || cooldown < current || cooldown - current <= 0 || location == null || owner == null)
                continue;

            addSpecialBlock(location, new ChunkLoader(owner, plugin, cooldown, location));
            loadedChunks.add(new ChunkPair(location.getChunk()));
        }
    }

}
