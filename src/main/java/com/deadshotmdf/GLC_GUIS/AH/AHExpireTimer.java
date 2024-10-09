package com.deadshotmdf.GLC_GUIS.AH;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AHExpireTimer extends BukkitRunnable {

    private final AHManager ahManager;
    private final Map<UUID, AHTransaction> transactions;
    private int occasionalCleanup;

    public AHExpireTimer(AHManager ahManager, Map<UUID, AHTransaction> transactions) {
        this.ahManager = ahManager;
        this.transactions = transactions;
    }

    public void run(){
        if(occasionalCleanup++ >= 2)
            try{cleanup();}
            catch(Throwable e){}
    }

    private void cleanup(){
        long current = System.currentTimeMillis();
        new HashMap<>(transactions).values().forEach(transaction -> {
            if(transaction == null || transaction.getExpire() < current)
                ahManager.itemRemoveExpire(transaction, true);
        });
        occasionalCleanup = 0;
    }

}
