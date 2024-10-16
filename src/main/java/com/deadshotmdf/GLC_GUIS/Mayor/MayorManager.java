package com.deadshotmdf.GLC_GUIS.Mayor;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.GLC_GUIS.Mayor.Enums.UpgradeType;
import com.deadshotmdf.GLC_GUIS.Mayor.Objects.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MayorManager extends AbstractGUIManager {

    private final Map<UpgradeType, Upgrade> upgrades;
    private final Map<UUID, Map<UpgradeType, Integer>> playerUpgrades;
    private final Map<UUID, DelayUpgradePair> delayedUpgrades;
    private GUI mayor;

    public MayorManager(GuiManager guiManager, JavaPlugin plugin) {
        super(guiManager, plugin, new File(plugin.getDataFolder(), "guis/mayor/"), new File(plugin.getDataFolder(), "data/mayor.yml"));
        this.upgrades = new EnumMap<>(UpgradeType.class);
        this.playerUpgrades = new HashMap<>();
        this.delayedUpgrades = new HashMap<>();
        loadInformation();
    }

    public void addDelayedUpgrade(UUID uuid, DelayUpgradePair pair) {
        delayedUpgrades.put(uuid, pair);
    }

    public void removeDelayedUpgrade(UUID uuid) {
        delayedUpgrades.remove(uuid);
    }

    public DelayUpgradePair getDelayedUpgrade(UUID uuid) {
        return delayedUpgrades.get(uuid);
    }

    public Map<UpgradeType, Integer> getPlayerUpgrades(UUID uuid) {
        return uuid == null ? null : playerUpgrades.computeIfAbsent(uuid, _ -> new EnumMap<>(UpgradeType.class));
    }

    public Integer getPlayerUpgrade(UUID uuid, UpgradeType type) {
        return getPlayerUpgrades(uuid).getOrDefault(type, 0);
    }

    public Upgrade getUpgrade(UpgradeType type) {
        return upgrades.get(type);
    }

    public void openGUI(Player player){
        UUID uuid = player.getUniqueId();
        DelayUpgradePair pair = delayedUpgrades.get(uuid);

        if(pair != null && pair.delay() - System.currentTimeMillis() <= 0){
            //msg maybe??
            delayedUpgrades.remove(uuid);
            advancePlayerLevel(uuid, pair.upgradeType());
        }

        if(mayor != null)
            guiManager.commenceOpen(player, mayor, null);
    }

    public boolean noLongerDelay(UUID uuid){
        DelayUpgradePair pair = delayedUpgrades.get(uuid);

        if(pair == null)
            return false;

        if(pair.delay() - System.currentTimeMillis() <= 0){
            delayedUpgrades.remove(uuid);
            return true;
        }

        return false;
    }

    public void startPlayerDelay(Upgrade upgrade, UUID uuid, UpgradeType upgradeType, int currentLevel){
        UpgradeLevel nextLevel;

        //Player level is from 1 to infinity
        //Upgrade level list is from 0 to infinity
        //To access the current level of a player you would do player level - 1
        try{nextLevel = upgrade.getLevels().get(currentLevel);}
        catch(IndexOutOfBoundsException ignored){return;}

        if(nextLevel.getDelay() <= 0 || upgrade.getMaxLevel() <= currentLevel){
            //If there's no delay just advance, or if the level is too high, set it to the max
            advancePlayerLevel(uuid, upgradeType);
            return;
        }

        if(getDelayedUpgrade(uuid) != null)
            return;

        addDelayedUpgrade(uuid, new DelayUpgradePair(upgradeType, System.currentTimeMillis() + nextLevel.getDelay() + 1000));
    }

    //Advances the players level. If the level is too high, set it to the maximum possible level for the upgrade type instead
    public void advancePlayerLevel(UUID uuid, UpgradeType type){
        noLongerDelay(uuid);
        if(getDelayedUpgrade(uuid) != null)
            return;

        Upgrade upgrade = upgrades.get(type);

        if(upgrade == null)
            return;

        Map<UpgradeType, Integer> playerUpgrades = getPlayerUpgrades(uuid);
        int level = playerUpgrades.getOrDefault(type, 0);
        playerUpgrades.put(type, Math.min(level + 1, upgrade.getMaxLevel()));
    }

    @Override
    protected GUI specifyGUI(boolean perPlayer, GuiManager guiManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> mergedPages, String type){
        return switch (type != null ? type.toUpperCase() : "null") {
            case "MAYOR" -> mayor = new MayorGUI(guiManager, this, title, size, mergedPages, null, null);
            default -> super.specifyGUI(perPlayer, guiManager, title, size, mergedPages, type);
        };
    }

    @Override
    public void saveInformation(){
        this.config.set("player_upgrades", null);
        this.config.set("delayed_upgrades", null);
        new HashMap<>(playerUpgrades).forEach((uuid, map) -> this.config.set("player_upgrades." + uuid.toString(), GUIUtils.serializeMap(map)));
        new HashMap<>(delayedUpgrades).forEach((uuid, pair) -> this.config.set("delayed_upgrades." + uuid.toString(), pair.toString()));
        this.saveC();
    }
    @SuppressWarnings("unchecked")
    @Override
    public void loadInformation(){
        for(String key : getKeys("player_upgrades", false)){
            UUID uuid = GUIUtils.getUUID(key);

            if(uuid != null)
                playerUpgrades.put(uuid, (Map<UpgradeType, Integer>) GUIUtils.deserializeEnumMap(config.getString("player_upgrades." + key), UpgradeType.class));
        }

        for(String key : getKeys("delayed_upgrades", false)){
            UUID uuid = GUIUtils.getUUID(key);
            DelayUpgradePair delayUpgradePair = DelayUpgradePair.fromString(config.getString("delayed_upgrades." + key));

            if(uuid != null && delayUpgradePair != null)
                this.delayedUpgrades.put(uuid, delayUpgradePair);
        }
    }

    @Override
    public void onReload(){
        upgrades.clear();
        FileConfiguration config = plugin.getConfig();

        ConfigurationSection sec = config.getConfigurationSection("upgrades");

        if(sec == null)
            return;

        for(String key : sec.getKeys(false)){
            UpgradeType type = UpgradeType.fromString(key);

            if(type == null)
                continue;

            List<UpgradeLevel> levels = new ArrayList<>();
            List<String> possibleLevels = sec.getStringList(key + ".levels");

            for(String possibleLevel : possibleLevels){
                UpgradeLevel level = readUpgradeLevel(possibleLevel);

                if(level != null)
                    levels.add(level);
            }

            upgrades.put(type, new Upgrade(type, levels, ConfigSettings.color(sec.getStringList(key + ".lore_main")), ConfigSettings.color(sec.getStringList(key + ".lore_upgrade_line"))));
        }
    }

    private UpgradeLevel readUpgradeLevel(String s){
        if(s == null)
            return null;

        String[] split = s.split(";");

        if(split.length != 5)
            return null;

        Double benefit = GUIUtils.getDouble(split[0]);
        Double cost = GUIUtils.getDouble(split[1]);
        Double glcoins = GUIUtils.getDouble(split[2]);
        Long delay = GUIUtils.getLong(split[3]);
        Double instant = GUIUtils.getDouble(split[4]);

        if(benefit == null || cost == null || glcoins == null || delay == null || instant == null)
            return null;

        return new UpgradeLevel(benefit, cost, glcoins, TimeUnit.MINUTES.toMillis(delay), instant);
    }

}
