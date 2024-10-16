package com.deadshotmdf.GLC_GUIS.Mayor;

import com.deadshotmdf.GLC_GUIS.GUIUtils;

public record DelayUpgradePair(UpgradeType upgradeType, long delay) {

    @Override
    public String toString(){
        return upgradeType.toString() + ";" + delay;
    }

    public static DelayUpgradePair fromString(String s){
        if(s == null)
            return null;

        String[] split = s.split(";");

        if(split.length != 2)
            return null;

        UpgradeType upgradeType = UpgradeType.fromString(split[0]);
        Long delay = GUIUtils.getLong(split[1]);

        if(upgradeType == null || delay == null)
            return null;

        return new DelayUpgradePair(upgradeType, delay);
    }

}
