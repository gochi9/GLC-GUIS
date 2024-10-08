package com.deadshotmdf.GLC_GUIS.BlackMarket.Objects;

public enum BlackmarketEvent {

    NOTHING,
    ROBBED,
    SCAMMED,
    MUGGED;

    public static BlackmarketEvent fromString(String s){
        if(s == null)
            return null;

        return switch (s.toUpperCase()) {
            case "NOTHING" -> NOTHING;
            case "ROBBED" -> ROBBED;
            case "SCAMMED" -> SCAMMED;
            case "MUGGED" -> MUGGED;
            default -> null;
        };
    }

}
