package com.deadshotmdf.GLC_GUIS.General.Managers;

import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonType;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;

import java.util.HashMap;
import java.util.Map;

public class ButtonRegistry {

    private final Map<ButtonType, GuiElement> buttons = new HashMap<>();

    public void registerButton(GuiElement button) {
        buttons.put(button.getButtonType(), button);
    }

    public GuiElement getButton(ButtonType buttonType) {
        return buttons.get(buttonType);
    }

}
