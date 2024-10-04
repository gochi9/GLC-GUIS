package com.deadshotmdf.GLC_GUIS;

import com.deadshotmdf.GLC_GUIS.ActionHouse.ActionHouseManager;
import com.deadshotmdf.GLC_GUIS.General.Commands.ReloadConfigCommand;
import com.deadshotmdf.GLC_GUIS.General.Listeners.GUIListener;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.GLC_GUIS.Shop.OpenShopCommand;
import com.deadshotmdf.GLC_GUIS.Shop.ShopManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class GLCGGUIS extends JavaPlugin {

    private static Economy economy;
    private GuiManager guiManager;
    private ShopManager shopManager;
    private ActionHouseManager actionHouseManager;

    @Override
    public void onEnable() {
        GUIUtils.getSlots("1");
        if (!setupEconomy()) {
            getLogger().severe("Vault economy not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.guiManager = new GuiManager();
        this.shopManager = new ShopManager(guiManager, this);
        this.actionHouseManager = new ActionHouseManager(guiManager, this);

        this.guiManager.reloadConfig();

        Bukkit.getPluginManager().registerEvents(new GUIListener(guiManager), this);

        this.getCommand("shop").setExecutor(new OpenShopCommand(guiManager));
        this.getCommand("glcguis").setExecutor(new ReloadConfigCommand(guiManager));

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

    public static Economy getEconomy() {
        return economy;
    }

    public GuiManager getGuiManager(){
        return guiManager;
    }
}