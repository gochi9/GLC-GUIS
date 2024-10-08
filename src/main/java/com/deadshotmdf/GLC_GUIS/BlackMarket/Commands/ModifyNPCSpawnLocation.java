package com.deadshotmdf.GLC_GUIS.BlackMarket.Commands;

import com.deadshotmdf.GLC_GUIS.BlackMarket.Managers.BlackMarketManager;
import com.deadshotmdf.GLC_GUIS.General.Commands.CommandType;
import com.deadshotmdf.GLC_GUIS.General.Commands.SubCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModifyNPCSpawnLocation extends SubCommand<BlackMarketManager> {

    public ModifyNPCSpawnLocation(BlackMarketManager manager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        super(manager, permission, commandType, argsRequired, commandHelpMessage, commandWrongSyntax);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String id = args[1].toLowerCase();
        boolean add = args[0].equalsIgnoreCase("addnpcloc");

        if(add){
            manager.getNPCSpawnPoints().put(id, ((Player)sender).getLocation());
            sender.sendMessage("Added location " + id + " to NPCSpawnPoints");
            return;
        }

        Location removed = manager.getNPCSpawnPoints().remove(id);
        sender.sendMessage(removed != null ? "Removed location with id " + id : "No location found with id " + id);
    }
}
