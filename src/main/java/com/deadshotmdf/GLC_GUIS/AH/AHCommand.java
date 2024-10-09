package com.deadshotmdf.GLC_GUIS.AH;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AHCommand implements CommandExecutor {

    private final AHManager ahManager;

    public AHCommand(AHManager ahManager) {
        this.ahManager = ahManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can open the shop.");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if(args.length == 0 || args.length == 2)
            ahManager.openGUI(player, args.length == 0);
        else if(args.length == 1 && item.getType() != Material.AIR)
            ahManager.addItem(player, item, 100, 100);
        return true;
    }
}
