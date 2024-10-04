package com.deadshotmdf.GLC_GUIS.General.GUI;

import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;

import java.util.Map;
import java.util.UUID;

public class SharedGUI extends AbstractGUI {

    public SharedGUI(GuiManager guiManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> pageElements, GUI backGUI) {
        super(guiManager, title, size, pageElements);
    }

    @Override
    public boolean isShared() { return true; }

    @Override
    protected GUI createNewInstance(UUID player, GUI backGUI, String... args) {
        return this;
    }

}
