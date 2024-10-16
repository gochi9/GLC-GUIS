package com.deadshotmdf.GLC_GUIS.BlackMarket.Listeners;

import com.deadshotmdf.GLC_GUIS.BlackMarket.Managers.BlackMarketManager;
import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import net.citizensnpcs.api.event.NPCEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NPCListener implements Listener {

    private final BlackMarketManager blackMarketManager;
    private final Set<UUID> warnings;

    public NPCListener(BlackMarketManager blackMarketManager) {
        this.blackMarketManager = blackMarketManager;
        warnings = new HashSet<>();
    }

    @EventHandler
    public void onNPCClick(NPCRightClickEvent ev) {click(ev, ev.getClicker());}

    @EventHandler
    public void onNPCClick(NPCLeftClickEvent ev) {click(ev, ev.getClicker());}

    private void click(NPCEvent ev, Player player){
        if(!ev.getNPC().equals(blackMarketManager.getNpc()))
            return;

        UUID uuid = player.getUniqueId();

        if(warnings.contains(uuid)){
            blackMarketManager.openGUI(player);
            return;
        }

        warnings.add(uuid);
        player.sendMessage(ConfigSettings.getWarnPlayer());
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1f, 1f);
    }

}
