package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks;

public enum SpecialBlockType {

    COLLECTOR,
    LOADER;

    public static SpecialBlockType getSpecialBlockType(String s){
        if(s == null)
            return null;

        switch(s.toUpperCase()){
            case "COLLECTOR":
                return COLLECTOR;
            case "LOADER":
                return LOADER;
            default:
                return null;
        }
    }

}
