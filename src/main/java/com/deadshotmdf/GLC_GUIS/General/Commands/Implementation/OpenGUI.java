package com.deadshotmdf.GLC_GUIS.General.Commands.Implementation;

import com.deadshotmdf.GLC_GUIS.General.Commands.CommandType;
import com.deadshotmdf.GLC_GUIS.General.Commands.SubCommand;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenGUI extends SubCommand<AbstractGUIManager> {

    public OpenGUI(AbstractGUIManager manager, GuiManager guiManager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        super(null, manager, guiManager, permission, commandType, argsRequired, commandHelpMessage, commandWrongSyntax);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = Bukkit.getPlayer(args[1]);

        if(player == null){
            sender.sendMessage("Player must be online");
            return;
        }

        GUI gui = guiManager.getGuiTemplate(args[2]);

        if(gui == null){
            sender.sendMessage("GUI does not exist");
            return;
        }

        guiManager.commenceOpen(player, gui, null);
    }
}
