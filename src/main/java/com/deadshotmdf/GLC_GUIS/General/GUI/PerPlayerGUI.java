package com.deadshotmdf.GLC_GUIS.General.GUI;

import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;

import java.util.Map;

public class PerPlayerGUI extends AbstractGUI {

    public PerPlayerGUI(GuiManager guiManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> pageElements, GUI backGUI) {
        super(guiManager, title, size, pageElements, backGUI);
    }

    @Override
    public boolean isShared() { return false; }

}
