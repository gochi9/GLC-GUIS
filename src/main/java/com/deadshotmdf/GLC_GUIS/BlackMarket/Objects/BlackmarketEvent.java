package com.deadshotmdf.GLC_GUIS.BlackMarket.Objects;

public enum BlackmarketEvent {

    NOTHING,
    ROBBED,
    SCAM,
    MUGGED;

    public static BlackmarketEvent fromString(String s){
        if(s == null)
            return null;

        return switch (s.toUpperCase()) {
            case "NOTHING" -> NOTHING;
            case "ROBBED" -> ROBBED;
            case "SCAM" -> SCAM;
            case "MUGGED" -> MUGGED;
            default -> null;
        };
    }

}
