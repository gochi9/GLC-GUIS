package com.deadshotmdf.GLC_GUIS.Shop;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.GenericShopChangeAmount;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.ReplaceableButton;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.GUI.PerPlayerGUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class GenericShopTransactionGUI extends PerPlayerGUI<ShopManager> {

    private final static String[] placeholders = {"{genericShopDisplayItemShowAmount}", "{valueLore}"};

    private final ItemStack item;
    private final boolean isBuy;
    private final Material material;
    private final String item_name;
    private final double buy_value;
    private final double sell_value;
    private final int max_buy;
    private final int max_sell;
    private int amount;

    public GenericShopTransactionGUI(GuiManager guiManager, ShopManager correspondentManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> pageElements, GUI backGUI, String... args) {
        super(guiManager, correspondentManager, title, size, pageElements, backGUI, args);
        this.isBuy = GUIUtils.retrieveFrom("buying", ":", args).equalsIgnoreCase("true");
        this.material = Material.getMaterial(GUIUtils.retrieveFrom("material", ":", args));
        this.item_name = GUIUtils.retrieveFrom("item_name", ":", args);
        this.buy_value = GUIUtils.getDoubleOrDefault(GUIUtils.retrieveFrom("buy_value", ":", args), 0.0);
        this.sell_value = GUIUtils.getDoubleOrDefault(GUIUtils.retrieveFrom("sell_value", ":", args), 0.0);
        this.max_buy = GUIUtils.getIntegerOrDefault(GUIUtils.retrieveFrom("max_buy", ":", args), 0);
        this.max_sell = GUIUtils.getIntegerOrDefault(GUIUtils.retrieveFrom("max_sell", ":", args), 0);
        this.item = new ItemStack(this.material == null ? Material.GLASS : this.material);
        this.amount = 1;
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(ConfigSettings.color(item_name));
        int maxStack = isBuy ? max_buy : max_sell;
        if(maxStack > 0)
            meta.setMaxStackSize(Math.max(maxStack, 99));

        this.item.setItemMeta(meta);
        try{this.refreshInventory();}
        catch (Throwable ignored){}
    }

    public void changeAmount(int amount, boolean add){
        this.amount = Math.min(isBuy ? max_buy : max_sell, this.amount + (add ? amount : -amount));
        this.amount = Math.max(1, this.amount);
        this.item.setAmount(this.amount < 100 ? this.amount : 99);
        refreshInventory();
    }

    @Override
    public void handleClick(InventoryClickEvent ev, Object... args) {
        super.handleClick(ev, isBuy, amount, isBuy ? buy_value : sell_value, item.getType());
        try{refreshInventory();}
        catch (Throwable ignored){}
    }

    @Override
    public void refreshInventory(){
        super.refreshInventory();

        Inventory inventory = pageInventories.get(0);

        if(inventory.isEmpty())
            return;

        String[] replacement = new String[]{ConfigSettings.getGenericShopDisplayItemShowAmount(amount), isBuy ?
                ConfigSettings.getGenericShopBuyLore(buy_value * (1.0 * amount)) :
                ConfigSettings.getGenericShopSellLore(sell_value * (1.0 * amount))};

        for(Map.Entry<Integer, GuiElement> elements : pageElements.get(0).entrySet())
            if(elements.getValue() instanceof ReplaceableButton replaceableButton)
                inventory.setItem(elements.getKey(), ReplaceableButton.createSimpleReplaceableButton(this.item, this.item_name, replaceableButton.getLoreClone()).getItemStackClone(placeholders, replacement));

            else if (elements.getValue() instanceof GenericShopChangeAmount button)
                inventory.setItem(elements.getKey(), noLongerUseful(button) ? null : button.getItemStackClone(null));
    }

    @Override
    protected GUI createNewInstance(UUID player, GUI backGUI, String... args) {
        return new GenericShopTransactionGUI(guiManager, correspondentManager, title, size, pageElements, backGUI, args);
    }

    private boolean noLongerUseful(GenericShopChangeAmount button){
        return ((button.isAdd() && (this.amount + button.getValue()) > (isBuy ? this.max_buy : max_sell)) || (!button.isAdd() && this.amount <= 1));
    }

}
