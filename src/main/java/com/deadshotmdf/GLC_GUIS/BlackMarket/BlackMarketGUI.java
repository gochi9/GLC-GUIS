package com.deadshotmdf.GLC_GUIS.BlackMarket;

import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.GUI.SharedGUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

public class BlackMarketGUI extends SharedGUI<BlackMarketManager> {

    public BlackMarketGUI(GuiManager guiManager, BlackMarketManager correspondentManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> pageElements) {
        super(guiManager, correspondentManager, title, size, pageElements);
    }

    @Override
    public void handleClick(InventoryClickEvent ev, Object... args){
        if(correspondentManager.isBlackMarketOngoing())
            super.handleClick(ev, args);
        else
            forceClose();
    }
}
