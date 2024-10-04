package com.deadshotmdf.GLC_GUIS.General.Buttons;

import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractButton implements GuiElement{

    protected final ItemStack item;
    protected final Object correspondentManager;
    protected final GuiManager guiManager;
    protected final String[] args;

    public AbstractButton(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args){
        this.item = item;
        this.correspondentManager = correspondentManager;
        this.guiManager = guiManager;
        this.args = args;
    }

    @Override
    public ItemStack getItemStackClone(String[] placeholder, String... replace) {
        ItemStack item = this.item.clone();

        if(placeholder == null || replace == null || placeholder.length == 0 || replace.length == 0 || placeholder.length != replace.length)
            return item;

        ItemMeta meta = item.getItemMeta();

        if(meta == null)
            return item;

        if(meta.hasDisplayName())
            meta.setDisplayName(StringUtils.replaceEach(meta.getDisplayName(), placeholder, replace));

        if(meta.hasLore())
            meta.setLore(replace(meta.getLore(), placeholder, replace));

        item.setItemMeta(meta);
        return item;
    }

    public abstract void onClick(InventoryClickEvent event, GUI gui, Object... args);

    private static List<String> replace(List<String> lore, String[] placeholder, String... replace){
        List<String> list = new ArrayList<>(lore.size());

        for(String s : lore)
            list.add(StringUtils.replaceEach(s, placeholder, replace));

        return list;
    }
}
