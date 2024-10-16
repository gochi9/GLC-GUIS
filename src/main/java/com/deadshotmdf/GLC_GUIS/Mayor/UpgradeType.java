package com.deadshotmdf.GLC_GUIS.Mayor;

public enum UpgradeType {

    UNLOCK_BANK,
    BANK,
    TAX,
    VAULT_PAGES,
    BANK_LIMIT,
    HOPPER_LIMIT,
    MOB_LOOT_MULTIPLIER,
    CROP_MULTIPLIER;

    public static UpgradeType fromString(String s){
        if(s == null)
            return null;

        return switch (s.toUpperCase()) {
            case "UNLOCK_BANK" -> UNLOCK_BANK;
            case "BANK" -> BANK;
            case "TAX" -> TAX;
            case "VAULT_PAGES" -> VAULT_PAGES;
            case "BANK_LIMIT" -> BANK_LIMIT;
            case "HOPPER_LIMIT" -> HOPPER_LIMIT;
            case "MOB_LOOT_MULTIPLIER" -> MOB_LOOT_MULTIPLIER;
            case "CROP_MULTIPLIER" -> CROP_MULTIPLIER;
            default -> null;
        };
    }

}
