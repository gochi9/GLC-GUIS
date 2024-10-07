package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.Shop;

import com.deadshotmdf.GLC_GUIS.GLCGGUIS;
import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@ButtonIdentifier("GENERIC_SHOP_CONFIRM_TRANSACTION")
public class GenericShopConfirmTransaction extends AbstractButton {

    public GenericShopConfirmTransaction(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
    }

    @Override
    public void onClick(InventoryClickEvent ev, GUI gui, Object... args) {
        boolean isBuy = (boolean) args[0];
        int amount = (int) args[1];
        double value = (double) args[2];
        Material material = (Material) args[3];
        Player player = (Player) ev.getWhoClicked();

        if(gui.getBackGUI() != null)
            guiManager.commenceOpen(player, gui.getBackGUI(), null);

        if(!isBuy)
            amount = searchAndRemove(amount, player.getInventory(), material);

        if(amount == 0)
            return;

        double money = amount * value;
        Economy economy = GLCGGUIS.getEconomy();

        if(isBuy && economy.getBalance(player) < money){
            player.sendMessage("poor fuck");
            return;
        }

        if(isBuy){
            economy.withdrawPlayer(player, money);
            giveItems(amount, material, player);
        }
        else
            economy.depositPlayer(player, money);
    }

    private void giveItems(int amount, Material material, Player player){
        World world = player.getWorld();
        Location location = player.getLocation();
        player.getInventory().addItem(new ItemStack(material, amount)).values().forEach(item -> world.dropItemNaturally(location, item));
    }

    private int searchAndRemove(int amount, PlayerInventory inventory, Material material) {
        int current = 0;

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);

            if (item == null || item.getType() != material)
                continue;

            int am = item.getAmount();
            int remaining = amount - current;

            if(am < remaining){
                inventory.setItem(i, null);
                current += am;
                continue;
            }

            if (am == remaining)
                inventory.setItem(i, null);

            else {
                item.setAmount(am - remaining);
                inventory.setItem(i, item);
            }
            current += remaining;
            break;
        }
        return current;
    }
}
