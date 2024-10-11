package com.deadshotmdf.GLC_GUIS.General.Commands;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GLCGGUIS;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class SubCommand<T extends AbstractGUIManager> {

    protected final GLCGGUIS main;
    protected final T manager;
    protected final GuiManager guiManager;
    private final String permission;
    private final CommandType commandType;
    private final int argsRequired;
    private final String commandHelpMessage;
    private final String commandWrongSyntax;

    public SubCommand(T manager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        this(null, manager, null, permission, commandType, argsRequired, commandHelpMessage, commandWrongSyntax);
    }

    public SubCommand(GLCGGUIS main, T manager, GuiManager guiManager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax){
        this.main = main;
        this.manager = manager;
        this.guiManager = guiManager;
        this.permission = permission;
        this.commandType = commandType;
        this.argsRequired = ++argsRequired;
        this.commandHelpMessage = commandHelpMessage;
        this.commandWrongSyntax = commandWrongSyntax;
    }

    protected boolean canExecute(CommandSender sender, int argsLength, boolean sendMessage){
        boolean isPlayer = sender instanceof Player;
        if(commandType == CommandType.PLAYER && !isPlayer){
            if(sendMessage)
                sender.sendMessage(ConfigSettings.getPlayersOnly());
            return false;
        }

        if(isPlayer && !sender.hasPermission(permission)){
            if(sendMessage)
                sender.sendMessage(ConfigSettings.getNoPermission());
            return false;
        }

        if(commandType == CommandType.CONSOLE && !(sender instanceof ConsoleCommandSender)){
            if(sendMessage)
                sender.sendMessage(ConfigSettings.getConsoleOnly());
            return false;
        }

        if(sendMessage && argsRequired > 1 && argsLength < argsRequired){
            sender.sendMessage(commandWrongSyntax);
            return false;
        }

        return true;
    }

    public abstract void execute(CommandSender sender, String[] args);
    public List<String> tabCompleter(CommandSender sender, String[] args){
        return GLCGUICommand.EMPTY;
    }

    public String getCommandHelpMessage(){
        return commandHelpMessage;
    }

}
