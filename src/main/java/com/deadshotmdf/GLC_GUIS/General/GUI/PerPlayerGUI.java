package com.deadshotmdf.GLC_GUIS.General.GUI;

import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;

import java.util.Map;
import java.util.UUID;

public class PerPlayerGUI<T extends AbstractGUIManager> extends AbstractGUI<T> {

    public PerPlayerGUI(GuiManager guiManager, T correspondentManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> pageElements, GUI backGUI, String... args) {
        super(guiManager, correspondentManager, title, size, pageElements, args);
        this.backGUI = backGUI;
    }

    @Override
    public boolean isShared() { return false; }

    @Override
    protected GUI createNewInstance(UUID player, GUI backGUI, String... args) {
        return new PerPlayerGUI<>(guiManager, correspondentManager, title, size, pageElements, backGUI, args);
    }

}
