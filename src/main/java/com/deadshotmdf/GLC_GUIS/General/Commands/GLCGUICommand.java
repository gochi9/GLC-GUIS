package com.deadshotmdf.GLC_GUIS.General.Commands;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GLCGGUIS;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class GLCGUICommand<T extends AbstractGUIManager> implements CommandExecutor, TabCompleter {

    public final static List<String> EMPTY = Collections.emptyList();

    protected final HashMap<String, SubCommand> subCommands;

    public GLCGUICommand(GLCGGUIS main, T manager) {
        this.subCommands = new HashMap<>();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1 || args[0].equalsIgnoreCase("help")){
            List<String> allowedCommands = getAllowedCommands(sender);

            if(allowedCommands.isEmpty())
                sender.sendMessage(ConfigSettings.getNoAvailableCommands());
            else
                allowedCommands.forEach(allowedCommand -> sender.sendMessage(subCommands.get(allowedCommand).getCommandHelpMessage()));

            return true;
        }

        SubCommand subCommand = subCommands.get(args[0]);

        if(subCommand == null){
            sender.sendMessage(ConfigSettings.getInvalidCommand());
            return true;
        }

        if(!subCommand.canExecute(sender, args.length, true))
            return true;

        subCommand.execute(sender, args);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0)
            return EMPTY;

        if(args.length == 1)
            return getAllowedCommands(sender);


        SubCommand subCommand = subCommands.get(args[0]);
        return subCommand != null && subCommand.canExecute(sender, 0, false) ? subCommand.tabCompleter(sender, args) : EMPTY;
    }

    private List<String> getAllowedCommands(CommandSender sender) {
        List<String> allowedCommands = new LinkedList<>();
        subCommands.forEach((k, v) -> {
            if(v.canExecute(sender, 0, false))
                allowedCommands.add(k);
        });

        return allowedCommands;
    }
}
