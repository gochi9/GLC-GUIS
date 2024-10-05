package com.deadshotmdf.GLC_GUIS.General.Commands;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GLCGGUIS;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadConfigCommand implements CommandExecutor {

    private final GLCGGUIS main;
    private final GuiManager guiManager;

    public ReloadConfigCommand(GLCGGUIS main, GuiManager guiManager) {
        this.main = main;
        this.guiManager = guiManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("glcguis.reload")) {
            //msg
            return true;
        }

        ConfigSettings.reloadConfig(main);
        this.guiManager.reloadConfig();
        //msg
        return true;
    }
}
