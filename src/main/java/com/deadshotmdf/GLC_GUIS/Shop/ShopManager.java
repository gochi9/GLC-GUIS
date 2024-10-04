package com.deadshotmdf.GLC_GUIS.Shop;

import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;

public class ShopManager extends AbstractGUIManager {

    public ShopManager(GuiManager guiManager, JavaPlugin plugin) {
        super(guiManager, plugin, new File(plugin.getDataFolder(), "guis/shop/"));
    }

    @Override
    protected GuiElement enhanceGuiElement(Map<String, String> extraValues, GuiElement element, String action, String[] args) {
        return element;
    }

    @Override
    protected GUI specifyGUI(boolean perPlayer, GuiManager guiManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> mergedPages, String type){
        return switch (type != null ? type.toUpperCase() : "null") {
            case "GENERIC_CONFIRMATION" -> new GenericShopTransactionGUI(guiManager, title, size, mergedPages, null);
            default -> super.specifyGUI(perPlayer, guiManager, title, size, mergedPages, type);
        };
    }

}
