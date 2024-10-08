package com.deadshotmdf.GLC_GUIS;

import com.deadshotmdf.GLC_GUIS.ActionHouse.ActionHouseManager;
import com.deadshotmdf.GLC_GUIS.BlackMarket.Commands.BlackmarketCommand;
import com.deadshotmdf.GLC_GUIS.BlackMarket.Managers.BlackMarketManager;
import com.deadshotmdf.GLC_GUIS.BlackMarket.Listeners.NPCListener;
import com.deadshotmdf.GLC_GUIS.General.Commands.ReloadConfigCommand;
import com.deadshotmdf.GLC_GUIS.General.Listeners.GUIListener;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.GLC_GUIS.Shop.OpenShopCommand;
import com.deadshotmdf.GLC_GUIS.Shop.ShopManager;
import com.deadshotmdf.gLCoins_Server.EconomyWrapper;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class GLCGGUIS extends JavaPlugin {

    private static EconomyWrapper economy;
    private GuiManager guiManager;
    private ShopManager shopManager;
    private ActionHouseManager actionHouseManager;
    private BlackMarketManager blackMarketManager;

    @Override
    public void onEnable() {
        ConfigSettings.reloadConfig(this);
        GUIUtils.getSlots("1");
        if (!setupEconomy()) {
            getLogger().severe("Vault economy not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.guiManager = new GuiManager();
        this.shopManager = new ShopManager(guiManager, this);
        this.actionHouseManager = new ActionHouseManager(guiManager, this);
        this.blackMarketManager = new BlackMarketManager(guiManager, this);

        this.guiManager.reloadConfig();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new GUIListener(guiManager), this);
        pm.registerEvents(new NPCListener(blackMarketManager), this);

        this.getCommand("shop").setExecutor(new OpenShopCommand(guiManager));
        this.getCommand("glcguis").setExecutor(new ReloadConfigCommand(this, guiManager));
        this.getCommand("blackmarket").setExecutor(new BlackmarketCommand(this, blackMarketManager));
    }

    @Override
    public void onDisable() {
        this.blackMarketManager.saveInformation();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = (EconomyWrapper) rsp.getProvider();
        return economy != null;
    }

    public static EconomyWrapper getEconomy() {
        return economy;
    }

    public GuiManager getGuiManager(){
        return guiManager;
    }
}