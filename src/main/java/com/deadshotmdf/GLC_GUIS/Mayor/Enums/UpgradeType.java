package com.deadshotmdf.GLC_GUIS.Mayor.Enums;

//NEVER MODIFY THE PACKAGE FOR THIS CLASS
public enum UpgradeType {

    UNLOCK_BANK,
    BANK,
    TAX,
    VAULT_PAGES,
    BANK_LIMIT,
    HOPPER_LIMIT,
    MOB_LOOT_MULTIPLIER,
    CHUNK_MEMBERS,
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
            case "CHUNK_MEMBERS" -> CHUNK_MEMBERS;
            case "CROP_MULTIPLIER" -> CROP_MULTIPLIER;
            default -> null;
        };
    }

}
