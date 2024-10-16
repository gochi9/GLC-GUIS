package com.deadshotmdf.GLC_GUIS.Mayor.Commands;

import com.deadshotmdf.GLC_GUIS.General.Commands.CommandType;
import com.deadshotmdf.GLC_GUIS.General.Commands.SubCommand;
import com.deadshotmdf.GLC_GUIS.Mayor.MayorManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MayorOpenOthers extends SubCommand<MayorManager> {

    public MayorOpenOthers(MayorManager manager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        super(manager, permission, commandType, argsRequired, commandHelpMessage, commandWrongSyntax);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = Bukkit.getPlayer(args[1]);

        if(player == null){
            sender.sendMessage("Needs online player");
            return;
        }

        manager.openGUI(player);
    }
}
