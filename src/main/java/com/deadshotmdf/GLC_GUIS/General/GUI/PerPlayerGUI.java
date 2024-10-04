package com.deadshotmdf.GLC_GUIS.General.GUI;

import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;

import java.util.Map;
import java.util.UUID;

public class PerPlayerGUI extends AbstractGUI {

    public PerPlayerGUI(GuiManager guiManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> pageElements, GUI backGUI, String... args) {
        super(guiManager, title, size, pageElements, args);
        this.backGUI = backGUI;
    }

    @Override
    public boolean isShared() { return false; }

    @Override
    protected GUI createNewInstance(UUID player, GUI backGUI, String... args) {
        return new PerPlayerGUI(guiManager, title, size, pageElements, backGUI, args);
    }

}
