package com.deadshotmdf.GLC_GUIS.General.Commands.Implementation;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GLCGGUIS;
import com.deadshotmdf.GLC_GUIS.General.Commands.CommandType;
import com.deadshotmdf.GLC_GUIS.General.Commands.SubCommand;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import org.bukkit.command.CommandSender;

public class ReloadConfig extends SubCommand<AbstractGUIManager> {

    public ReloadConfig(GLCGGUIS main, AbstractGUIManager manager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        super(main, manager, null, permission, commandType, argsRequired, commandHelpMessage, commandWrongSyntax);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ConfigSettings.reloadConfig(main);
        this.guiManager.reloadConfig();
        sender.sendMessage(ConfigSettings.getReloadConfig());
    }
}
