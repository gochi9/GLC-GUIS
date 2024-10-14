package com.deadshotmdf.GLC_GUIS.BankGUI;

import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.GUI.SharedGUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;

public class BankGUIManager extends AbstractGUIManager {

    private GUI bankGUI;

    public BankGUIManager(GuiManager guiManager, JavaPlugin plugin) {
        super(guiManager, plugin, new File(plugin.getDataFolder(), "guis/bankGUI/"), new File(plugin.getDataFolder(), "data/bankGUI.yml"));
    }

    public void openGUI(Player player){
        if(bankGUI != null)
            guiManager.commenceOpen(player, bankGUI, null);
    }

    @Override
    protected GUI specifyGUI(boolean perPlayer, GuiManager guiManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> mergedPages, String type){
        return switch (type != null ? type.toUpperCase() : "null") {
            case "BANK_GUI" -> (bankGUI = new SharedGUI<>(guiManager, this, title, size, mergedPages));
            default -> super.specifyGUI(perPlayer, guiManager, title, size, mergedPages, type);
        };
    }

}
