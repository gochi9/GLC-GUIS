package com.deadshotmdf.GLC_GUIS.Shop;

import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.ReplaceableButton;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.GUI.PerPlayerGUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.UUID;

public class GenericShopTransactionGUI extends PerPlayerGUI {

    private final ItemStack item;
    private final boolean isBuy;
    private final Material material;
    private final String item_name;
    private final double buy_value;
    private final double sell_value;
    private final int max_buy;
    private final int max_sell;
    private int amount;

    public GenericShopTransactionGUI(GuiManager guiManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> pageElements, GUI backGUI, String... args) {
        super(guiManager, title, size, pageElements, backGUI, args);
        this.isBuy = Boolean.parseBoolean(GUIUtils.retrieveFrom("buying", ":", args));
        this.material = Material.getMaterial(GUIUtils.retrieveFrom("material", ":", args).substring(1));
        this.item_name = GUIUtils.retrieveFrom("item_name", ":", args).substring(1);
        this.buy_value = GUIUtils.getDoubleOrDefault(GUIUtils.retrieveFrom("buy_value", ":", args), 0.0);
        this.sell_value = GUIUtils.getDoubleOrDefault(GUIUtils.retrieveFrom("sell_value", ":", args), 0.0);
        this.max_buy = GUIUtils.getIntegerOrDefault(GUIUtils.retrieveFrom("max_buy", ":", args), 0);
        this.max_sell = GUIUtils.getIntegerOrDefault(GUIUtils.retrieveFrom("max_sell", ":", args), 0);
        this.item = new ItemStack(this.material == null ? Material.GLASS : this.material);
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(item_name == null ? " " : item_name);
        this.item.setItemMeta(meta);
        this.refreshInventory();
    }

    public void changeAmount(int amount, boolean add){
        this.amount = Math.max(isBuy ? max_buy : max_sell, this.amount + (add ? amount : -amount));
        this.item.setAmount(this.amount);
        refreshInventory();
    }

    @Override
    public void handleClick(InventoryClickEvent ev) {
        super.handleClick(ev);
        try{refreshInventory();}
        catch (Throwable ignored){}
    }

    @Override
    public void refreshInventory(){
        super.refreshInventory();

        Inventory inventory = pageInventories.get(0);

        if(inventory.isEmpty())
            return;

        for(Map.Entry<Integer, GuiElement> elements : pageElements.get(0).entrySet())
            if(elements.getValue() instanceof ReplaceableButton)
                inventory.setItem(elements.getKey(), ReplaceableButton.createSimpleReplaceableButton(this.item).getItemStackClone(null));
    }

    @Override
    protected GUI createNewInstance(UUID player, GUI backGUI, String... args) {
        return new GenericShopTransactionGUI(guiManager, title, size, pageElements, backGUI, args);
    }

}
