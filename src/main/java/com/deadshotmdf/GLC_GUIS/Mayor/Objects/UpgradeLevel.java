package com.deadshotmdf.GLC_GUIS.Mayor.Objects;

public class UpgradeLevel {

    private final double benefit;
    private final double cost;
    private final double glcoins;
    private final long delay;
    private final double instant_unlock;

    public UpgradeLevel(double benefit, double cost, double glcoins, long delay, double instantUnlock) {
        this.benefit = benefit;
        this.cost = cost;
        this.glcoins = glcoins;
        this.delay = delay;
        this.instant_unlock = instantUnlock;
    }

    public double getBenefit() {
        return benefit;
    }

    public double getCost() {
        return cost;
    }

    public double getGlcoins() {
        return glcoins;
    }

    public long getDelay() {
        return delay;
    }

    public double getInstant_unlock() {
        return instant_unlock;
    }

}
