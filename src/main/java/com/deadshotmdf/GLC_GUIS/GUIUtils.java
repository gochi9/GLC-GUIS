package com.deadshotmdf.GLC_GUIS;

import java.util.HashSet;
import java.util.Set;

public class GUIUtils {

    public static Integer getInteger(String s){
        try{
            return Integer.parseInt(s);
        }
        catch (Throwable ignored){
            return null;
        }
    }

    public static Set<Integer> getSlots(String from){
        Set<Integer> slots = new HashSet<>();

        if(from == null)
            return slots;

        String[] possibleSlots = from.split(",");

        if(possibleSlots.length == 0){
            getSlotsFromDash(from, slots);
            return slots;
        }

        for(String possibleSlot : possibleSlots){
            if(possibleSlot.contains("-")){
                getSlotsFromDash(possibleSlot, slots);
                continue;
            }

            Integer slot = getInteger(possibleSlot);

            if(slot != null)
                slots.add(slot);
        }

        return slots;
    }

    private static void getSlotsFromDash(String dash, Set<Integer> slots){
        String[] possibleRange = dash.split("-");

        if(possibleRange.length != 2)
            return;

        Integer from = getInteger(possibleRange[0]);
        Integer to = getInteger(possibleRange[1]);

        if(from == null || to == null)
            return;

        int min = Math.min(from, to);
        int max = Math.max(from, to);

        for(int i = min; i <= max; i++)
            slots.add(i);
    }

}
