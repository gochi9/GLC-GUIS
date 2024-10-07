package com.deadshotmdf.GLC_GUIS.BlackMarket;

import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.Label;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class BlackMarketManager extends AbstractGUIManager {

  //  private final HashMap<UUID, BlackmarketPair> availableLootTable;

    public BlackMarketManager(GuiManager guiManager, JavaPlugin plugin) {
        super(guiManager, plugin, new File(plugin.getDataFolder(), "guis/blackmarket/"));
     //   this.availableLootTable = new HashMap<>();
    }

    @Override
    protected GuiElement enhanceGuiElement(String specialType, ItemStack item, Map<String, String> extraValues, GuiElement element, String action, String[] args) {
        if(specialType == null || !specialType.equalsIgnoreCase("blackmarket") || action.equalsIgnoreCase("BLACK_MARKET_BUY_ITEM"))
            return element;

        Double min_price = GUIUtils.getDouble("min_price");
        Double max_price = GUIUtils.getDouble("max_price");

        if(min_price == null || max_price == null)
            return new Label(item, this, guiManager, args, extraValues);

    //    availableLootTable.put(GUIUtils.getUniqueID(availableLootTable), new BlackmarketPair(item, Math.min(min_price, max_price), Math.max(min_price, max_price)));
        return null;
    }

}
