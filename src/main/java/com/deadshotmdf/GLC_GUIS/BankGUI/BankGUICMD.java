package com.deadshotmdf.GLC_GUIS.BankGUI;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BankGUICMD implements CommandExecutor {

    private final BankGUIManager bankGUIManager;

    public BankGUICMD(BankGUIManager bankGUIManager){
        this.bankGUIManager = bankGUIManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            sender.sendMessage("console command");
            return true;
        }

        if(args.length == 0){
            sender.sendMessage("Needs a player");
            return true;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if(player == null){
            sender.sendMessage("Player not found");
            return true;
        }

        bankGUIManager.openGUI(player);
        return true;
    }
}
