package com.deadshotmdf.GLC_GUIS.General.Buttons;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractButton implements GuiElement{

    protected ItemStack item;
    protected String name;
    protected List<String> lore;
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
        this.permission = elementData != null ? elementData.get("permission") : null;
        this.permissionMessage = elementData != null ? elementData.get("permissionMessage") : null;

        ItemMeta meta = item.getItemMeta();
        boolean hasItemMeta = item.hasItemMeta();
        this.name = hasItemMeta ? meta.getDisplayName() : null;
        this.lore = hasItemMeta ? meta.getLore() : null;
    }

    @Override
    public ItemStack getItemStackClone(){
        return this.item.clone();
    }

    @Override
    public ItemStack getItemStackClone(String[] placeholder, String... replace) {
        return getItemStackClone(null, placeholder, replace);
    }

    @Override
    public ItemStack getItemStackClone(ItemStack clone, String[] placeholder, String... replace) {
        ItemStack item = clone != null ? clone.clone() : this.item.clone();

        if(placeholder == null || replace == null || placeholder.length == 0 || replace.length == 0 || placeholder.length != replace.length)
            return item;

        ItemMeta meta = item.getItemMeta();

        if(meta == null)
            return item;

        if(name != null)
            meta.setDisplayName(StringUtils.replaceEach(name, placeholder, replace));

        if(lore != null)
            meta.setLore(replace(lore, placeholder, replace));

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public ItemStack getItemStackMarkedAndReplaced(NamespacedKey key, PersistentDataType type, Object value, String[] placeholder, String... replace){
        ItemStack item = this.item.clone();

        ItemMeta meta = item.getItemMeta();

        if(meta == null || key == null || type == null || value == null)
            return item;

        meta.getPersistentDataContainer().set(key, type, value);
        item.setItemMeta(meta);
        return getItemStackClone(item, placeholder, replace);
    }

    @Override
    public void addInitialMark(NamespacedKey key, PersistentDataType type, Object value){
        this.item = getItemStackMarkedAndReplaced(key, type, value, null);
    }

    public void addExtra(String... extra){}

    @Override
    public boolean canClick(HumanEntity player){
        boolean canClick = permission == null || permission.isBlank() || player.hasPermission(permission);

        if(!canClick && permissionMessage != null)
            ConfigSettings.sendExtraMessage(player, permissionMessage);

        return canClick;
    }

    public abstract void onClick(InventoryClickEvent event, GUI gui, Object... args);

    public List<String> getLoreClone(){
        return new ArrayList<>(lore);
    }

    public static List<String> replace(List<String> lore, String[] placeholder, String... replace){
        List<String> list = new ArrayList<>(lore.size());

        for(String s : lore)
            list.add(StringUtils.replaceEach(s, placeholder, replace));

        return list;
    }
}
