package com.deadshotmdf.GLC_GUIS.BlackMarket.Managers;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class BlackMarketTimer extends BukkitRunnable {

    private final BlackMarketManager manager;
    private final Random random;
    private int lastHour;
    private long cooldown;
    private Long blackMarketStay;
    private boolean firstTime;

    public BlackMarketTimer(BlackMarketManager manager) {
        this.manager = manager;
        this.random = new Random();
        this.lastHour = -1;
        this.blackMarketStay = null;
        this.cooldown = 0;
        this.firstTime = true;
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();

        if(blackMarketStay != null){
            if(currentTime > blackMarketStay)
                removeBlackMarket();

            return;
        }

        if (currentTime < cooldown)
            return;

        Calendar calendar = GregorianCalendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        if (lastHour == currentHour)
            return;

        lastHour = currentHour;

        if(random.nextInt(100) + 1 <= ConfigSettings.getBlackMarketSpawnChance() || firstTime)
            spawnBlackMarket(currentTime);
    }

    private void spawnBlackMarket(long currentTime) {
        blackMarketStay = currentTime + ConfigSettings.getBlackMarketStayDuration();
        firstTime = false;
        manager.spawnBlackMarket();
    }

    private void removeBlackMarket() {
        blackMarketStay = null;
        cooldown = System.currentTimeMillis() + ConfigSettings.getBlackMarketCooldown();
        manager.removeBlackMarket();
    }
}
