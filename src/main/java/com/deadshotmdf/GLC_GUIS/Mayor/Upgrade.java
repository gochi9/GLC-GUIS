package com.deadshotmdf.GLC_GUIS.Mayor;

import java.util.List;

public class Upgrade {

    private final UpgradeType type;
    private final List<UpgradeLevel> levels;
    private final List<String> main_lore;
    private final List<String> upgrade_lore;

    public Upgrade(UpgradeType type, List<UpgradeLevel> levels, List<String> mainLore, List<String> upgradeLore) {
        this.type = type;
        this.levels = levels;
        this.main_lore = mainLore;
        this.upgrade_lore = upgradeLore;
    }

    public UpgradeType getType() {
        return type;
    }

    public List<UpgradeLevel> getLevels() {
        return levels;
    }

    public List<String> getMain_lore() {
        return main_lore;
    }

    public List<String> getUpgrade_lore() {
        return upgrade_lore;
    }

    public int getMaxLevel(){
        return levels.size();
    }

}
