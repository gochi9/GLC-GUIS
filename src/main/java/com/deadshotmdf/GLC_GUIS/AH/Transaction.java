package com.deadshotmdf.GLC_GUIS.AH;

import org.bukkit.inventory.ItemStack;

public class Transaction {

    private final ItemStack item;
    private final long expire;
    private final double sellAmount;
    private final double getGLCoinsSellAmount;
    private boolean isStillValid;

    public Transaction(ItemStack item, long expire, double sellAmount, double getGLCoinsSellAmount) {
        this.item = item;
        this.expire = expire;
        this.sellAmount = sellAmount;
        this.getGLCoinsSellAmount = getGLCoinsSellAmount;
        this.isStillValid = true;
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

}
