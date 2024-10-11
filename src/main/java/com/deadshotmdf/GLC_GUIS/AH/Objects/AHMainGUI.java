package com.deadshotmdf.GLC_GUIS.AH.Objects;

import com.deadshotmdf.GLC_GUIS.AH.AHManager;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;

import java.util.Map;
import java.util.UUID;

public class AHMainGUI extends AhSharedGUI {

    public AHMainGUI(GuiManager guiManager, AHManager correspondentManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> pageElements, GUI backGUI, UUID viewer, Object... args) {
        super(guiManager, correspondentManager, title, size, pageElements, backGUI, viewer, args);
    }

    @Override
    public boolean isMainAHGUI() {
        return true;
    }

    @Override
    protected GUI createNewInstance(UUID uuid, GUI backGUI, Object... args) {
        return new AHMainGUI(guiManager, correspondentManager, title, size, pageElements, backGUI, uuid, args);
    }
}
