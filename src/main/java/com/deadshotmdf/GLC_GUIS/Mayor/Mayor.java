package com.deadshotmdf.GLC_GUIS.Mayor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Mayor implements CommandExecutor {

    private final MayorManager mayorManager;

    public Mayor(MayorManager mayorManager) {
        this.mayorManager = mayorManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        mayorManager.openGUI((Player) sender);
        return true;
    }
}
