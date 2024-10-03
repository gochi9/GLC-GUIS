package com.deadshotmdf.GLC_GUIS;

import com.deadshotmdf.GLC_GUIS.General.Managers.ButtonRegistry;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class GLCGGUIS extends JavaPlugin {

    private Economy economy;
    private GuiManager guiManager;
    private ButtonRegistry buttonRegistry;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe("Vault economy not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.guiManager = new GuiManager();
        this.buttonRegistry = new ButtonRegistry();
    }

    @Override
    public void onDisable() {

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }

    public GuiManager getGuiManager(){
        return guiManager;
    }
}