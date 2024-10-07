package com.deadshotmdf.GLC_GUIS.General.GUI;

import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;

import java.util.Map;
import java.util.UUID;

public class SharedGUI<T extends AbstractGUIManager> extends AbstractGUI<T> {

    public SharedGUI(GuiManager guiManager, T correspondentManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> pageElements) {
        super(guiManager, correspondentManager, title, size, pageElements);
    }

    @Override
    public boolean isShared() { return true; }

    @Override
    protected GUI createNewInstance(UUID player, GUI backGUI, String... args) {
        return this;
    }

}
