package com.deadshotmdf.GLC_GUIS.Shop;

import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ShopManager extends AbstractGUIManager {

    public ShopManager(GuiManager guiManager, JavaPlugin plugin) {
        super(guiManager, plugin, new File(plugin.getDataFolder(), "guis/shop/"));
    }

}
