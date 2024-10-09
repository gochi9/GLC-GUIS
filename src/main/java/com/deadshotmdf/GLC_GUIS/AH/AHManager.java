package com.deadshotmdf.GLC_GUIS.AH;

import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class AHManager extends AbstractGUIManager {


    public AHManager(GuiManager guiManager, JavaPlugin plugin) {
        super(guiManager, plugin, new File(plugin.getDataFolder(), "guis/ah/"), new File(plugin.getDataFolder(), "data/ah.yml"));
    }
}
