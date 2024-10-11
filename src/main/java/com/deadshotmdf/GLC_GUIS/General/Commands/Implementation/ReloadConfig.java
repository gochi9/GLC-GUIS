package com.deadshotmdf.GLC_GUIS.General.Commands.Implementation;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GLCGGUIS;
import com.deadshotmdf.GLC_GUIS.General.Commands.CommandType;
import com.deadshotmdf.GLC_GUIS.General.Commands.SubCommand;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.command.CommandSender;

public class ReloadConfig extends SubCommand<AbstractGUIManager> {

    public ReloadConfig(GLCGGUIS main, AbstractGUIManager manager, GuiManager guiManager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        super(main, manager, guiManager, permission, commandType, argsRequired, commandHelpMessage, commandWrongSyntax);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ConfigSettings.reloadConfig(main);
        this.guiManager.reloadConfig();
        sender.sendMessage(ConfigSettings.getReloadConfig());
    }
}
