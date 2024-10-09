package com.deadshotmdf.GLC_GUIS.AH;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class AHTransaction {

    private final UUID transactionID;
    private final UUID publisher;
    private final ItemStack item;
    private final long expire;
    private final double sellAmount;
    private final double getGLCoinsSellAmount;
    private boolean isStillValid;
    private boolean exists;

    public AHTransaction(UUID transactionID, UUID publisher, ItemStack item, long expire, double sellAmount, double getGLCoinsSellAmount) {
        this.transactionID = transactionID;
        this.publisher = publisher;
        this.item = item;
        this.expire = expire;
        this.sellAmount = sellAmount;
        this.getGLCoinsSellAmount = getGLCoinsSellAmount;
        this.isStillValid = true;
        this.exists = true;
    }

    public AHTransaction(UUID publisher, ItemStack item){
        this.transactionID = UUID.randomUUID();
        this.publisher = publisher;
        this.item = item;
        this.expire = 0;
        this.sellAmount = 0;
        this.getGLCoinsSellAmount = 0;
        this.isStillValid = false;
        this.exists = true;
    }

    public UUID getTransactionID() {
        return transactionID;
    }

    public UUID getPublisher() {
        return publisher;
    }

    public ItemStack getItem() {
        return item;
    }

    public long getExpire() {
        return expire;
    }

    public double getSellAmount() {
        return sellAmount;
    }

    public double getGLCoinsSellAmount() {
        return getGLCoinsSellAmount;
    }

    public boolean isStillValid() {
        return isStillValid;
    }

    public void setNoLongerValid(){
        this.isStillValid = false;
    }

    public boolean doesExist(){
        return exists;
    }

    public void setNoLongerExists(){
        this.exists = false;
    }

}
