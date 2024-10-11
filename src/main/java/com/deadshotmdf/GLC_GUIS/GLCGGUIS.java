package com.deadshotmdf.GLC_GUIS;

import com.deadshotmdf.GLC_GUIS.AH.AHManager;
import com.deadshotmdf.GLC_GUIS.AH.Commands.AHCommand;
import com.deadshotmdf.GLC_GUIS.BlackMarket.Commands.BlackmarketCommand;
import com.deadshotmdf.GLC_GUIS.BlackMarket.Managers.BlackMarketManager;
import com.deadshotmdf.GLC_GUIS.BlackMarket.Listeners.NPCListener;
import com.deadshotmdf.GLC_GUIS.General.Commands.Implementation.GenericGUISCommand;
import com.deadshotmdf.GLC_GUIS.General.Listeners.GUIListener;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.GLC_GUIS.Shop.OpenShopCommand;
import com.deadshotmdf.GLC_GUIS.Shop.ShopManager;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Commands.GiveChunkLoaderCommand;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Listeners.BlockPlaceLis;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Listeners.CancelSpecialBlockInteraction;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Listeners.CheckExpiredLoaderInvLis;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Listeners.SpecialBlockRightClick;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocksManager;
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
    private AHManager actionHouseManager;
    private BlackMarketManager blackMarketManager;
    private SpecialBlocksManager specialBlocksManager;

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
        this.actionHouseManager = new AHManager(guiManager, this);
        this.blackMarketManager = new BlackMarketManager(guiManager, this);
        this.specialBlocksManager = new SpecialBlocksManager(guiManager, this, this.shopManager.getPrices());

        this.guiManager.reloadConfig();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new GUIListener(guiManager), this);
        pm.registerEvents(new NPCListener(blackMarketManager), this);
        pm.registerEvents(new CancelSpecialBlockInteraction(specialBlocksManager), this);
        pm.registerEvents(new BlockPlaceLis(this, specialBlocksManager), this);
        pm.registerEvents(new CheckExpiredLoaderInvLis(specialBlocksManager), this);
        pm.registerEvents(new GUIListener(guiManager), this);
        pm.registerEvents(new SpecialBlockRightClick(specialBlocksManager), this);

        this.getCommand("shop").setExecutor(new OpenShopCommand(guiManager));
        this.getCommand("glcguis").setExecutor(new GenericGUISCommand(this, null, guiManager));
        this.getCommand("blackmarket").setExecutor(new BlackmarketCommand(this, blackMarketManager));
        this.getCommand("ah").setExecutor(new AHCommand(this, actionHouseManager));
        this.getCommand("giveloader").setExecutor(new GiveChunkLoaderCommand(specialBlocksManager));
    }

    @Override
    public void onDisable() {
        guiManager.saveAll();
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

}