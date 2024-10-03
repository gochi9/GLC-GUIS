package com.deadshotmdf.GLC_GUIS.General.Buttons;

import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractActionButton extends AbstractButton{

    protected final GuiManager guiManager;

    public AbstractActionButton(@NotNull ItemStack item, GuiManager guiManager, Object correspondentManager) {
        super(item, correspondentManager);
        this.guiManager = guiManager;
    }

}
