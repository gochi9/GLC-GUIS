package com.deadshotmdf.GLC_GUIS.CSV;

import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CsvCommand implements CommandExecutor {
    private final Plugin plugin;

    public CsvCommand(Plugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        switch (strings[0]) {
            case "default":
                if(commandSender instanceof Player) {
                    commandSender.sendMessage("Only the console can issue this command!");
                    return false;
                }
                DefaultCSV.generateDefaultCSV(this.plugin.getDataFolder());
                return true;
        }
        return false;
    }
}
