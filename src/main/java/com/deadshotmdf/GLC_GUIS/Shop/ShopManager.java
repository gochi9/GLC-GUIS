package com.deadshotmdf.GLC_GUIS.Shop;

import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;

public class ShopManager extends AbstractGUIManager {

    private final EnumMap<Material, Double> prices;

    public ShopManager(GuiManager guiManager, JavaPlugin plugin) {
        super(guiManager, plugin, new File(plugin.getDataFolder(), "guis/shop/"), new File(plugin.getDataFolder(), "data/shop.yml"));
        this.prices = new EnumMap<>(Material.class);
    }

    public Double getMaterialPrice(Material material) {
        return prices.get(material);
    }

    public EnumMap<Material, Double> getPrices() {
        return prices;
    }

    @Override
    protected GuiElement enhanceGuiElement(String specialType, ItemStack item, Map<String, String> extraValues, GuiElement element, String action, String[] args) {
        if(item == null)
            return element;

        double sell = GUIUtils.getDoubleOrDefault(extraValues.get("sell_value"), 0.0);

        if(sell > 0.000)
            prices.put(item.getType(), sell);

        return element;
    }

    @Override
    protected GUI specifyGUI(boolean perPlayer, GuiManager guiManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> mergedPages, String type){
        return switch (type != null ? type.toUpperCase() : "null") {
            case "GENERIC_CONFIRMATION" -> new GenericShopTransactionGUI(guiManager, this, title, size, mergedPages, null, null);
            default -> super.specifyGUI(perPlayer, guiManager, title, size, mergedPages, type);
        };
    }

    @Override
    public void onReload(){
        this.prices.clear();
    }

}
