package com.deadshotmdf.GLC_GUIS.AH.Commands;

import com.deadshotmdf.GLC_GUIS.AH.AHManager;
import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GLCGGUIS;
import com.deadshotmdf.GLC_GUIS.General.Commands.CommandType;
import com.deadshotmdf.GLC_GUIS.General.Commands.GLCGUICommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AHCommand extends GLCGUICommand<AHManager> {

    public AHCommand(GLCGGUIS main, AHManager manager) {
        super(main, manager);
        this.subCommands.put("sell", new AHSell(manager, "glcguis.ahsell", CommandType.PLAYER, 1, ConfigSettings.getAHSellHelpMessage(), ConfigSettings.getAHSellSyntax()));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0 && sender instanceof Player player) {
            manager.openGUI(player, true);
            return true;
        }

        return super.onCommand(sender, cmd, label, args);
    }

}
