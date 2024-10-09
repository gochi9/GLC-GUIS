package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.AH;

import com.deadshotmdf.GLC_GUIS.AH.Objects.AhSharedGUI;
import com.deadshotmdf.GLC_GUIS.AH.Objects.SortType;
import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Map;

@ButtonIdentifier("AH_SORT")
public class AHSort extends AbstractButton {

    private long cooldown;

    public AHSort(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
    }

    @Override
    public void onClick(InventoryClickEvent event, GUI gui, Object... args) {
        if(!(gui instanceof AhSharedGUI ahSharedGUI) || cooldown - System.currentTimeMillis() >= 0)
            return;

        cooldown = System.currentTimeMillis() + 250;
        ahSharedGUI.setCurrentSortType();
        updateLore(ahSharedGUI);
    }

    public void updateLore(AhSharedGUI ahSharedGUI){
        this.lore = new LinkedList<>(ConfigSettings.getAHSortLore());
        this.lore.replaceAll(string -> {
            SortType type = SortType.fromString(string);

            if(type == null)
                return null;

            return type == ahSharedGUI.getCurrentSortType() ? (ChatColor.WHITE + type.getMessage()) : ChatColor.GRAY + type.getMessage();
        });
        this.item = getItemStackClone(new String[]{"tetetetest"}, "tetettetst");
    }
}
