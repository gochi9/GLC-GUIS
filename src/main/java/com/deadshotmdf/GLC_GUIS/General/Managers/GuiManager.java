package com.deadshotmdf.GLC_GUIS.General.Managers;

import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.Shop.GenericShopTransactionGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.*;

public class GuiManager {

    private final Map<String, GUI> guiTemplates;
    private final Map<UUID, GUI> openGuis;
    private final Set<AbstractGUIManager> managers;

    public GuiManager() {
        this.guiTemplates = new HashMap<>();
        this.openGuis = new HashMap<>();
        this.managers = new HashSet<>();
    }

    public void registerGuiTemplate(String name, GUI gui) {
        guiTemplates.put(name, gui);
    }

    public GUI getGuiTemplate(String name) {
        return guiTemplates.get(name);
    }

    public void openGui(HumanEntity player, String guiName, GUI backGUI, String... args) {
        GUI gui = guiTemplates.get(guiName.toLowerCase());

        if(gui == null){
            player.sendMessage(ChatColor.RED + "GUI not found: " + guiName);
            return;
        }

        commenceOpen(player, gui, backGUI, args);
    }

    public void commenceOpen(HumanEntity player, GUI gui, GUI backGUI, String... args){
        UUID uuid = player.getUniqueId();
        GUI newGUI = gui.createInstance(uuid, backGUI, args);

        try{removeOpenGui(player, true);}
        catch (Throwable ignored){}

        newGUI.open(player, 0);
        openGuis.put(uuid, newGUI);
    }

    public GUI getOpenGui(UUID uuid) {
        return openGuis.get(uuid);
    }

    public void removeOpenGui(HumanEntity player, boolean onCloseEvent) {
        UUID uuid = player.getUniqueId();
        GUI gui = getOpenGui(uuid);

        if(gui != null && !gui.isShared())
            gui.deletePages();

        if(!onCloseEvent)
            player.closeInventory();

        openGuis.remove(uuid);
    }

    public void addManager(AbstractGUIManager manager){
        this.managers.add(manager);
    }

    public void reloadConfig(){
        Bukkit.getOnlinePlayers().forEach(HumanEntity::closeInventory);
        openGuis.clear();
        guiTemplates.clear();
        this.managers.forEach(AbstractGUIManager::loadGUIsRecursive);
    }

}

