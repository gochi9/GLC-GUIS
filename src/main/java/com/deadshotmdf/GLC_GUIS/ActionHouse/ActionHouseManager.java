package com.deadshotmdf.GLC_GUIS.ActionHouse;

import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.GLC_GUIS.Shop.GenericShopTransactionGUI;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;

public class ActionHouseManager extends AbstractGUIManager {

    public ActionHouseManager(GuiManager guiManager, JavaPlugin plugin) {
        super(guiManager, plugin, new File(plugin.getDataFolder(), "guis/ah/"));
    }

    @Override
    protected GuiElement enhanceGuiElement(String specialType, ItemStack item, Map<String, String> extraValues, GuiElement element, String action, String[] args) {
        return element;
    }

}
