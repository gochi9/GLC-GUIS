package com.deadshotmdf.GLC_GUIS.Mayor.Commands;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GLCGGUIS;
import com.deadshotmdf.GLC_GUIS.General.Commands.CommandType;
import com.deadshotmdf.GLC_GUIS.General.Commands.GLCGUICommand;
import com.deadshotmdf.GLC_GUIS.General.Commands.SubCommand;
import com.deadshotmdf.GLC_GUIS.Mayor.MayorManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MayorCommand extends GLCGUICommand<MayorManager> {

    public MayorCommand(GLCGGUIS main, MayorManager manager) {
        super(main, manager);
        this.subCommands.put("open", new MayorOpenSelf(manager, "glcguis.mayoropenself", CommandType.PLAYER, 0, "", ""));
        this.subCommands.put("others", new MayorOpenOthers(manager, "glcguis.mayoropenother", CommandType.CONSOLE, 1, "", "/mayor others [player]"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length > 0)
            return super.onCommand(sender, cmd, label, args);

        SubCommand subCommand = subCommands.get("open");

        if(subCommand == null){
            sender.sendMessage(ConfigSettings.getInvalidCommand());
            return true;
        }

        if(!subCommand.canExecute(sender, args.length, true))
            return true;

        subCommand.execute(sender, args);
        return true;
    }
}
