package com.deadshotmdf.GLC_GUIS.General.Managers;

import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;

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

    public void unregisterGuiTemplate(String name) {
        guiTemplates.remove(name);
    }

    public GUI getGuiTemplate(String name) {
        return guiTemplates.get(name != null ? name.toLowerCase() : "NULL");
    }

    public void openGui(HumanEntity player, String guiName, GUI backGUI, String... args) {
        GUI gui = getGuiTemplate(guiName);

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

        newGUI.open(player, 0, true);
        openGuis.put(uuid, newGUI);
    }

    public GUI getOpenGui(UUID uuid) {
        return openGuis.get(uuid);
    }

    public void removeOpenGui(HumanEntity player, boolean onCloseEvent) {
        if(!onCloseEvent)
            player.closeInventory();

        openGuis.remove(player.getUniqueId());
    }

    public void addManager(AbstractGUIManager manager){
        this.managers.add(manager);
    }

    public void reloadConfig(){
        Bukkit.getOnlinePlayers().forEach(HumanEntity::closeInventory);
        openGuis.clear();
        guiTemplates.clear();
        this.managers.forEach(manager -> {
            manager.onReload();
            manager.loadGUIsRecursive();
        });
    }

    public void refreshInventories(Class<?>... cls) {
        if(cls.length == 0)
            return;

        for (GUI gui : openGuis.values())
            for(Class<?> cl : cls)
                if (cl.isAssignableFrom(gui.getClass()))
                    gui.refreshInventory();
    }

}

