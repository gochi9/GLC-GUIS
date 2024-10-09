package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.AH;

import com.deadshotmdf.GLC_GUIS.AH.AHManager;
import com.deadshotmdf.GLC_GUIS.AH.AHTransaction;
import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.GLCGGUIS;
import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.gLCoins_Server.GLCoinsS;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

@ButtonIdentifier("AH_TRANSACTION_BUTTON")
public class AHTransactionButton extends AbstractButton {

    private static final String[] placeholders = {"{date}", "{price}", "{glcoins}"};

    private final AHManager ahManager;
    private final AHTransaction transaction;
    private final UUID viewer;
    private boolean isPublisher;

    public AHTransactionButton(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
        this.ahManager = (AHManager) correspondentManager;
        this.transaction = ahManager.getTransaction(GUIUtils.getUUID(elementData.get("transaction_id")));
        this.viewer = GUIUtils.getUUID(elementData.get("viewer"));

        if(transaction == null)
            return;

        this.isPublisher = viewer.equals(transaction.getPublisher());
        this.lore = this.lore != null ? this.lore : new LinkedList<>();
        this.lore.addAll(ConfigSettings.getAhListingItemLore());
        this.lore.addAll(isPublisher ? ConfigSettings.getAhListingItemPublisher() : ConfigSettings.getAhListingItemBuyer());
        this.item = getItemStackClone(placeholders, GUIUtils.convertMillisToDate(transaction.getExpire()), transaction.getSellAmount()+"", transaction.getGLCoinsSellAmount()+"");
    }

    @Override
    public void onClick(InventoryClickEvent ev, GUI gui, Object... args) {
        HumanEntity whoClicked = ev.getWhoClicked();
        if(transaction == null || !transaction.isStillValid()){
            whoClicked.sendMessage(ConfigSettings.getTransactionNoLonger());
            return;
        }

        ClickType click = ev.getClick();

        if(isPublisher){
            if(click != ClickType.RIGHT)
                return;

            if(transaction.isStillValid())
                ahManager.itemRemoveExpire(transaction, false);
            else
                whoClicked.sendMessage(ConfigSettings.getTransactionNoLonger());

            return;
        }

        boolean isGLCoinsPay = click == ClickType.SHIFT_RIGHT;

        if(!isGLCoinsPay && click != ClickType.LEFT)
            return;

        double price = transaction.getGLCoinsSellAmount();
        double glcoins = transaction.getGLCoinsSellAmount();
        if(isGLCoinsPay && glcoins <= 0.000 || !isGLCoinsPay && price <= 0.000)
            return;

        if(isGLCoinsPay)
            GLCoinsS.getDatabase().getEntryPartialAsync(viewer, viewer, null).thenAccept(amount -> pay(whoClicked, amount, glcoins, false));
        else
            pay(whoClicked, GLCGGUIS.getEconomy().getBalance((OfflinePlayer) whoClicked), price, true);
    }

    private void pay(HumanEntity player, double balance, double toPay, boolean normalPay){
        if(!transaction.isStillValid()){
            player.sendMessage(ConfigSettings.getTransactionNoLonger());
            return;
        }

        boolean stillOnline = player != null && ((Player)player).isOnline();

        if(balance < toPay){
            if(stillOnline)
                player.sendMessage(ConfigSettings.getNotEnoughFunds(toPay, balance));

            return;
        }

        ahManager.itemBuy(transaction, (Player) player, normalPay ? toPay : 0, normalPay ? 0 : toPay);
    }

}
