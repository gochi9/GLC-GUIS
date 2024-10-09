package com.deadshotmdf.GLC_GUIS.AH.Objects;

public enum SortType {

    PRICE_MAX("Sort by highest price"),
    PRICE_MIN("Sort by lowest price"),
    GLCOINS_PRICE_MAX("Sort by highest GLCoins amount"),
    GLCOINS_PRICE_MIN("Sort by lowest GLCoins amount"),
    NEWEST_ITEMS("Sort by recently listed items"),
    OLDEST_ITEMS("Sort by oldest listed items");

    private final String message;
    SortType(String message) {
        this.message = message;
    }

    public SortType getNext(){
        return switch (this) {
            case PRICE_MAX -> PRICE_MIN;
            case PRICE_MIN -> GLCOINS_PRICE_MAX;
            case GLCOINS_PRICE_MAX -> GLCOINS_PRICE_MIN;
            case GLCOINS_PRICE_MIN -> NEWEST_ITEMS;
            case NEWEST_ITEMS -> OLDEST_ITEMS;
            default -> PRICE_MAX;
        };
    }

    public static SortType fromString(String s){
        if(s == null)
            return null;

        return switch (s.trim().toUpperCase()) {
            case "PRICE_MAX" -> PRICE_MAX;
            case "PRICE_MIN" -> PRICE_MIN;
            case "GLCOINS_PRICE_MAX" -> GLCOINS_PRICE_MAX;
            case "GLCOINS_PRICE_MIN" -> GLCOINS_PRICE_MIN;
            case "NEWEST_ITEMS" -> NEWEST_ITEMS;
            case "OLDEST_ITEMS" -> OLDEST_ITEMS;
            default -> null;
        };
    }

    public String getMessage(){
        return message;
    }

}
