package com.deadshotmdf.GLC_GUIS.BlackMarket.Commands;

import com.deadshotmdf.GLC_GUIS.BlackMarket.Managers.BlackMarketManager;
import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GLCGGUIS;
import com.deadshotmdf.GLC_GUIS.General.Commands.CommandType;
import com.deadshotmdf.GLC_GUIS.General.Commands.GLCGUICommand;

public class BlackmarketCommand extends GLCGUICommand<BlackMarketManager> {

    public BlackmarketCommand(GLCGGUIS main, BlackMarketManager manager) {
        super(main, manager);
        this.subCommands.put("open", new OpenBlackmarket(manager, "glcguis.blackmarketopen", CommandType.PLAYER, 0, ConfigSettings.getOpenBlackmarketGUIHelpMessage(), ""));
        this.subCommands.put("listnpclocs", new ListNPCSpawnLocation(manager, "glcguis.blackmarketlistnpcspawn", CommandType.BOTH, 0, "See the list and ids for the possible NPC spawn points", ""));
        this.subCommands.put("addnpcloc", new ModifyNPCSpawnLocation(manager, "glcguis.blackmarketmodifynpcspawn", CommandType.PLAYER, 1, "adds a location to the possible black market npc spawn location", "/blackmarket addnpcloc [id]"));
        this.subCommands.put("removenpcloc", new ModifyNPCSpawnLocation(manager, "glcguis.blackmarketmodifynpcspawn", CommandType.BOTH, 1, "removes a location with a certain id from the spawn points of the npc", "/blackmarket removenpcloc [id]"));
        this.subCommands.put("status", new BlackmarketStatus(manager, "glcguis.blackmarketstatus", CommandType.BOTH, 0, ConfigSettings.getBlackMarketStatusHelpMessage(), ""));
    }

}
