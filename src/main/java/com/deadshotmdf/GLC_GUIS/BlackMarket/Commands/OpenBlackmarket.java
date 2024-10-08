package com.deadshotmdf.GLC_GUIS.BlackMarket.Commands;

import com.deadshotmdf.GLC_GUIS.BlackMarket.Managers.BlackMarketManager;
import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.General.Commands.CommandType;
import com.deadshotmdf.GLC_GUIS.General.Commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenBlackmarket extends SubCommand<BlackMarketManager>{

    public OpenBlackmarket(BlackMarketManager manager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        super(manager, permission, commandType, argsRequired, commandHelpMessage, commandWrongSyntax);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!manager.isBlackMarketOngoing())
            sender.sendMessage(ConfigSettings.getNoBlackmarket());
        else
            manager.openGUI((Player) sender);
    }
}
