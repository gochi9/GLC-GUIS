package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GLCGGUIS;
import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.SpecialChunkBlocks.GiveableSpecialChunkBlock;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.API.SpecialBlockRemoveEvent;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.GUI.CollectorGUI;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.GUI.LoaderGUI;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Misc.ChunkPair;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Misc.SpecialBlockUtils;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.ChunkHopper;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.ChunkLoader;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.SpecialBlockType;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.SpecialChunkBlock;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Timers.LoaderTimer;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SpecialBlocksManager extends AbstractGUIManager {

    public final static String[] loaderPlaceholers = new String[]{"{date}"};
    private final Map<Location, SpecialChunkBlock> blocks;
    private final HashMap<ChunkPair, ChunkHopper> collectors;
    private final HashSet<ChunkPair> loadedChunks;
    private final EnumMap<Material, Double> sellValues;
    private final NamespacedKey specialKey, loaderKey;
    private GiveableSpecialChunkBlock loaderItem, collectorItem;
    private GUI loaderGUI, collectorGUI;

    public SpecialBlocksManager(GuiManager guiManager, JavaPlugin plugin, EnumMap<Material, Double> sellValues) {
        super(guiManager, plugin, new File(plugin.getDataFolder(), "guis/specialblocks/"), new File(plugin.getDataFolder(), "data/specialblocks.yml"));
        this.blocks = new ConcurrentHashMap<>();
        this.collectors = new HashMap<>();
        this.loadedChunks = new HashSet<>();
        this.sellValues = sellValues;
        this.specialKey = new NamespacedKey(plugin, "itf4if-3ffd-g3t-t3yp33e_plg445in");
        this.loaderKey = new NamespacedKey(plugin, "l0ade3er-k33kyd-plgguin43jf-34rf");
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

    public void addPlayerItem(Player player, Location location, ItemStack item){
        HashMap<Integer, ItemStack> remains =  player.getInventory().addItem(item);

        if(!remains.isEmpty())
            remains.values().forEach(remain -> player.getWorld().dropItem(location, remain));
    }

    ////////////////////////////////
    // CHUNK LOADER SECTION START //
    ////////////////////////////////
    ////////////////////////////////
    // CHUNK LOADER SECTION START //
    ////////////////////////////////
    ////////////////////////////////
    // CHUNK LOADER SECTION START //
    ////////////////////////////////

    public ItemStack getLoaderItem(long cooldown) {
        return loaderItem.getItemStackMarkedAndReplaced(loaderKey, PersistentDataType.LONG, cooldown, loaderPlaceholers, ConfigSettings.formatDate(cooldown));
    }

    public void removeLoader(Location location, ChunkLoader block) {
        block.removeBlock(plugin, true);
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
        addPlayerItem(player, location, getLoaderItem(cooldown));
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

    //////////////////////////////
    // CHUNK LOADER SECTION END //
    //////////////////////////////
    //////////////////////////////
    // CHUNK LOADER SECTION END //
    //////////////////////////////
    //////////////////////////////
    // CHUNK LOADER SECTION END //
    //////////////////////////////

    ///////////////////////////////////
    // CHUNK COLLECTOR SECTION START //
    ///////////////////////////////////
    ///////////////////////////////////
    // CHUNK COLLECTOR SECTION START //
    ///////////////////////////////////
    ///////////////////////////////////
    // CHUNK COLLECTOR SECTION START //
    ///////////////////////////////////

    public void placeCollector(Location location, UUID uuid){
        if(blocks.containsKey(location) || getChunkCollector(location) != null)
            return;

        ChunkHopper collector = new ChunkHopper(uuid, location, new EnumMap<>(Material.class));
        addSpecialBlock(location, collector);
        collectors.put(new ChunkPair(location.getChunk()), collector);
    }

    public ChunkHopper getChunkCollector(Location location){
        return collectors.get(new ChunkPair(location.getChunk()));
    }

    //The event that calls this needs to be LOW priority
    public boolean addItem(ChunkHopper collector, ItemStack item, Location location){
        if(collector == null || item == null || location == null)
            return false;

        if(sellValues.getOrDefault(item.getType(), 0.000) <= 0.000)
            return false;

        if(item.getItemMeta() instanceof ItemMeta meta && (meta.hasDisplayName() || meta.hasLore()))
            return false;

        int maxAmount = GUIUtils.getHighestPermissionNumber(null, collector.getOwner(), "glcguis.chunkcollectorcollectsize.");

        if(maxAmount == 0)
            return false;

        EnumMap<Material, Integer> values = collector.getValues();
        Material material = item.getType();
        int amount = values.getOrDefault(material, 0);

        if(amount >= maxAmount)
            return false;

        int toADD = amount + item.getAmount();

        if(toADD <= maxAmount){
            values.put(material, toADD);
            updateCollectorGUI(collector);
            return true;
        }

        values.put(material, maxAmount);
        location.getWorld().dropItemNaturally(location, new ItemStack(material, toADD - maxAmount));
        updateCollectorGUI(collector);
        return true;
    }

    public void onRightClickCollector(Location location, Player player, ChunkHopper block) {
        if(block.getOwner().equals(player.getUniqueId()))
            guiManager.commenceOpen(player, collectorGUI, null, location, block);
    }

    public ItemStack giveCollector(){
        return collectorItem.getItemStackClone();
    }

    public void removeCollector(Player player, Location location){
        if(location == null || !(getSpecialBlock(location) instanceof ChunkHopper chunkHopper))
            return;

        removeBlock(location);
        collectors.remove(new ChunkPair(location.getChunk()));
        sellAll(chunkHopper, player, false, null);
        chunkHopper.removeBlock(plugin, true);
        addPlayerItem(player, location, giveCollector());
    }

    public void sellAll(ChunkHopper chunkHopper, Player player, boolean refresh, GUI gui){
        chunkHopper.trimUselessInfo();

        if(chunkHopper.getValues().isEmpty())
            return;

        double fin = getSellAllValue(chunkHopper);
        if(fin <= 0.000)
            return;

        GLCGGUIS.getEconomy().depositPlayer(player, fin);
        chunkHopper.getValues().clear();

        if(refresh && gui != null)
            gui.refreshInventory();
    }

    public double getSellAllValue(ChunkHopper chunkHopper){
        AtomicDouble total = new AtomicDouble(0.000);
        new HashMap<>(chunkHopper.getValues()).forEach((k, v) ->{
            if(v < 1)
                return;

            double value = sellValues.getOrDefault(k, 0.000);

            if(value <= 0.000)
                return;

            total.set(total.get() + ((v*1d)*value));
        });
        return total.get();
    }

    private void updateCollectorGUI(ChunkHopper collector){
        GUI collectorGUI = collector.getCollectorGUI();

        if(collectorGUI != null)
            collectorGUI.refreshInventory();
    }

    /////////////////////////////////
    // CHUNK COLLECTOR SECTION END //
    /////////////////////////////////
    /////////////////////////////////
    // CHUNK COLLECTOR SECTION END //
    /////////////////////////////////
    /////////////////////////////////
    // CHUNK COLLECTOR SECTION END //
    /////////////////////////////////

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
                loaderItem = (GiveableSpecialChunkBlock) element;
                break;
            case COLLECTOR:
                collectorItem = (GiveableSpecialChunkBlock) element;
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
            case "CHUNK_COLLECTOR" -> (collectorGUI = new CollectorGUI(guiManager, this, title, size, mergedPages, null, null));
            default -> super.specifyGUI(perPlayer, guiManager, title, size, mergedPages, type);
        };
    }

    public EnumMap<Material, Double> getPrices(){
        return sellValues;
    }

    @Override
    public void saveInformation(){
        AtomicInteger i = new AtomicInteger(0);
        config.set("loaders", null);
        config.set("collectors", null);
        saveC();
        new HashMap<>(getBlocks()).forEach((k, v) ->{
            try{saveSpecialBlock(i, v, k);}
            catch(Throwable e){e.printStackTrace();}
        });

        saveC();
        loadedChunks.clear();
        collectors.clear();
        blocks.clear();
    }

    @Override
    public void loadInformation(){
        loadLoader();
        loadCollector();
    }

    private void saveSpecialBlock(AtomicInteger i, SpecialChunkBlock specialChunkBlock, Location k){
        if(specialChunkBlock == null)
            return;

        boolean isLoader = specialChunkBlock instanceof ChunkLoader;

        if(!isLoader)
            ((ChunkHopper)specialChunkBlock).trimUselessInfo();

        String path = (isLoader ? "loaders." : "collectors.") + i.getAndIncrement();
        config.set(path + ".owner", specialChunkBlock.getOwner().toString());
        config.set(path + ".location", k);
        config.set(path + (isLoader ? ".cooldown" : ".items"), isLoader ? ((ChunkLoader)specialChunkBlock).getCooldown() : GUIUtils.serializeMap(((ChunkHopper)specialChunkBlock).getValues()));

        specialChunkBlock.removeBlock(plugin, false);
    }

    private void loadLoader(){
        long current = System.currentTimeMillis();
        for(String key : getKeys("loaders", false)){
            String path = "loaders." + key;
            long cooldown = config.getLong(path + ".cooldown");
            Location location = config.getLocation(path + ".location");
            UUID owner = GUIUtils.getUUID(config.getString(path + ".owner"));

            if(cooldown <= 0 || cooldown < current || cooldown - current <= 0 || location == null || owner == null)
                continue;

            addSpecialBlock(location, new ChunkLoader(owner, plugin, cooldown, location));
            loadedChunks.add(new ChunkPair(location.getChunk()));
        }
    }

    private void loadCollector(){
        for(String key : getKeys("collectors", false)){
            String path = "collectors." + key;
            Location location = config.getLocation(path + ".location");
            UUID owner = GUIUtils.getUUID(config.getString(path + ".owner"));

            if(location == null || owner == null)
                continue;

            String map = config.getString(path + ".items");

            @SuppressWarnings("unchecked")
            EnumMap<Material, Integer> stored = map != null ? (EnumMap<Material, Integer>) GUIUtils.deserializeEnumMap(map, Material.class) : null;

            ChunkHopper chunkHopper = new ChunkHopper(owner, location, stored != null ? stored : new EnumMap<>(Material.class));
            addSpecialBlock(location, chunkHopper);
            collectors.put(new ChunkPair(location.getChunk()), chunkHopper);
        }
    }

}
