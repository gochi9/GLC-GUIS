package com.deadshotmdf.GLC_GUIS.AH.Commands;

import com.deadshotmdf.GLC_GUIS.AH.AHManager;
import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Commands.CommandType;
import com.deadshotmdf.GLC_GUIS.General.Commands.SubCommand;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AHSell extends SubCommand<AHManager> {

    public AHSell(AHManager manager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        super(manager, permission, commandType, argsRequired, commandHelpMessage, commandWrongSyntax);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        int limit = manager.getListingsPlayer(player.getUniqueId());
        int max = GUIUtils.getHighestPermissionNumber(player, "glcguis.ahlistings.");

        ItemStack item = player.getInventory().getItemInMainHand();

        if(item.getType() == Material.AIR){
            player.sendMessage(ConfigSettings.getAHSellMustHoldItem());
            return;
        }

        double sell_price = Math.max(GUIUtils.getDoubleOrDefault(args[1], 0.000), 0.000);
        double glcoins = args.length > 2 ? Math.max(GUIUtils.getDoubleOrDefault(args[2], 0.000), 0.000) : 0.000;

        if(sell_price <= 0.000 || glcoins <= 0.000){
            player.sendMessage(ConfigSettings.getAHSellInvalidPries());
            return;
        }

        if(limit >= max){
            player.sendMessage(ConfigSettings.getAHSellNoMoreSpace());
            return;
        }

        manager.addItem(player, item, sell_price, glcoins);
    }
}
