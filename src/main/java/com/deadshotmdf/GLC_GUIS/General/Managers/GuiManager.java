package com.deadshotmdf.GLC_GUIS.General.Managers;

import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuiManager {

    private final Map<String, GUI> guiTemplates = new HashMap<>();
    private final Map<UUID, GUI> openGuis = new HashMap<>();
   // private final Set<>

    public void registerGuiTemplate(String name, GUI gui) {
        guiTemplates.put(name, gui);
    }

    public GUI getGuiTemplate(String name) {
        return guiTemplates.get(name);
    }

    public void openGui(Player player, String guiName) {
        GUI gui = guiTemplates.get(guiName);

        if(gui == null){
            player.sendMessage(ChatColor.RED + "GUI not found: " + guiName);
            return;
        }

        UUID uuid = player.getUniqueId();
        gui = gui.createInstance(uuid);

        try{removeOpenGui(player, true);}
        catch (Throwable ignored){}

        gui.open(player, 0);
        openGuis.put(uuid, gui);
    }

    public GUI getOpenGui(UUID uuid) {
        return openGuis.get(uuid);
    }

    public void removeOpenGui(Player player, boolean onCloseEvent) {
        UUID uuid = player.getUniqueId();
        GUI gui = getOpenGui(uuid);

        if(gui != null && !gui.isShared())
            gui.deletePages();

        if(!onCloseEvent)
            player.closeInventory();

        openGuis.remove(uuid);
    }

}

