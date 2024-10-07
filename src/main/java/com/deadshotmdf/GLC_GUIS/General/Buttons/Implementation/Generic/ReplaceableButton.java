package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.Generic;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@ButtonIdentifier("REPLACEABLE")
public class ReplaceableButton extends AbstractButton {

    public ReplaceableButton(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
    }

    public static ReplaceableButton createSimpleReplaceableButton(@NotNull ItemStack item, String name, List<String> lore) {
        ItemStack i = item.clone();
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(ConfigSettings.color(name));
        meta.setLore(ConfigSettings.color(lore));
        i.setItemMeta(meta);
        return new ReplaceableButton(i, null, null, null, null);
    }

    public static ReplaceableButton createReplaceableButton(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        return new ReplaceableButton(item, correspondentManager, guiManager, args, elementData);
    }

    @Override
    public void onClick(InventoryClickEvent event, GUI gui, Object... args) {
    }
}
