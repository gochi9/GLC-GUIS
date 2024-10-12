package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Commands;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Misc.SpecialBlockUtils;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocksManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveChunkLoaderCommand implements CommandExecutor {

    private final SpecialBlocksManager chunkLoaderManager;

    public GiveChunkLoaderCommand(SpecialBlocksManager chunkLoaderManager) {
        this.chunkLoaderManager = chunkLoaderManager;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.hasPermission("chunkspecialblocks.giveloader")){
            sender.sendMessage(ConfigSettings.getNoPermission());
            return true;
        }

        if(args.length < 2){
            //msg
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null){
            //msg
            return true;
        }

        Long cooldown = SpecialBlockUtils.parseDuration(args[1]);

        if(cooldown == null){
            //msg
            return true;
        }

        chunkLoaderManager.addPlayerItem(target, target.getLocation(), chunkLoaderManager.getLoaderItem(cooldown));
        return true;
    }
}
