package com.deadshotmdf.GLC_GUIS.BlackMarket.Objects;

import com.deadshotmdf.GLC_GUIS.BlackMarket.Managers.BlackMarketManager;
import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.Blackmarket.BlackMarketItemButton;
import com.deadshotmdf.GLC_GUIS.General.GUI.SharedGUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class BlackMarketGUI extends SharedGUI<BlackMarketManager> {

    private final Map<Integer, GuiElement> decorations;

    public BlackMarketGUI(GuiManager guiManager, BlackMarketManager correspondentManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> pageElements) {
        super(guiManager, correspondentManager, title, size, pageElements);
        this.decorations = new HashMap<>(pageElements.get(0));
    }

    @Override
    public void handleClick(InventoryClickEvent ev, Object... args){
        if(correspondentManager.isBlackMarketOngoing())
            super.handleClick(ev, args);
        else
            forceClose();
    }

    public void startBlackMarket(List<BlackmarketPair> pairSet){
        Iterator<BlackmarketPair> iterator = pairSet.iterator();

        for(int i = 0; i < size && iterator.hasNext(); i++){
            if(decorations.containsKey(i))
                continue;

            BlackmarketPair pair = iterator.next();
            Map<String, String> elementData = new HashMap<>();
            elementData.put("material", pair.itemStack().getType().toString());
            elementData.put("sell_value", String.valueOf(GUIUtils.getRandomDouble(pair.min_price(), pair.max_price())));
            pageElements.computeIfAbsent(0, _ -> new HashMap<>()).put(i, new BlackMarketItemButton(pair.itemStack(), correspondentManager, guiManager, new String[0], elementData));
        }

        super.refreshInventory();
    }

    public void stopBlackMarket(){
        deletePages();
        forceClose();
        Inventory inv = pageInventories.get(0);
        decorations.forEach((slot, element) -> inv.setItem(slot, element.getItemStackClone(null)));
        this.pageElements.clear();
        this.pageElements.put(0, decorations);
    }
}
