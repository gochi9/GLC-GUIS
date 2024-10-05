package com.deadshotmdf.GLC_GUIS.General.Buttons;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractButton implements GuiElement{

    protected ItemStack item;
    protected final Object correspondentManager;
    protected final GuiManager guiManager;
    protected final String[] args;
    protected final Map<String, String> elementData;
    protected final String permission;
    protected final String permissionMessage;

    public AbstractButton(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData){
        this.item = item;
        this.correspondentManager = correspondentManager;
        this.guiManager = guiManager;
        this.args = args;
        this.elementData = elementData;
        this.permission = GUIUtils.retrieveFrom("permission", ":", args);
        this.permissionMessage = GUIUtils.retrieveFrom("permissionMessage", ":", args);
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

    public void addExtra(String... extra){}

    @Override
    public boolean canClick(HumanEntity player){
        boolean canClick = permission == null || player.hasPermission(permission);

        if(!canClick && permissionMessage != null)
            ConfigSettings.sendExtraMessage(player, permissionMessage);

        return canClick;
    }

    public abstract void onClick(InventoryClickEvent event, GUI gui, Object... args);

    private static List<String> replace(List<String> lore, String[] placeholder, String... replace){
        List<String> list = new ArrayList<>(lore.size());

        for(String s : lore)
            list.add(StringUtils.replaceEach(s, placeholder, replace));

        return list;
    }
}
