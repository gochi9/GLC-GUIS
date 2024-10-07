package com.deadshotmdf.GLC_GUIS;

import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.Buttons.TriFunction;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Logger;

public class GUIUtils {

    private static final DecimalFormat df = new DecimalFormat("0.000");
    private static final Map<String, TriFunction<ItemStack, Object, GuiManager, String[], Map<String, String>, AbstractButton>> buttonMap = new HashMap<>();

    static{
        Logger logger = Bukkit.getLogger();

        ClassLoader[] classLoaders = new ClassLoader[] {
                AbstractButton.class.getClassLoader(),
                Thread.currentThread().getContextClassLoader()
        };

        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forPackage("com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation", classLoaders))
                        .setScanners(new SubTypesScanner(false))
                        .filterInputsBy(new FilterBuilder().includePackage("com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation"))
                        .addClassLoaders(classLoaders)
        );

        Set<Class<? extends AbstractButton>> buttons = reflections.getSubTypesOf(AbstractButton.class);

        for (Class<? extends AbstractButton> button : buttons) {
            if(button.isInterface() || Modifier.isAbstract(button.getModifiers()))
                continue;

            ButtonIdentifier action = button.getAnnotation(ButtonIdentifier.class);

            if(action == null)
                continue;

            String actionValue = action.value();

            if(actionValue == null || actionValue.isBlank())
                continue;

            buttonMap.put(actionValue, (itemStack, correspondantManager, guiManager, args, map) -> {
                try {
                    Constructor<? extends AbstractButton> constructor = button.getConstructor(ItemStack.class, Object.class, GuiManager.class, String[].class, Map.class);
                    return constructor.newInstance(itemStack, correspondantManager, guiManager, args, map);
                }
                catch (Throwable e) {
                    logger.severe("Failed to instantiate action: " + actionValue);
                    logger.severe(e.getMessage());
                    return null;
                }
            });
        }

        buttonMap.entrySet().removeIf(entry -> entry.getValue() == null);
        logger.info("Loaded " + buttonMap.size() + " buttons");
    }

    public static String getDigits(double number) {
        String formattedNumber = df.format(number);

        return formattedNumber.endsWith(".000") ? formattedNumber.substring(0, formattedNumber.indexOf('.')) : formattedNumber;
    }

    public static UUID getUniqueID(HashMap<UUID, ?> map){
        UUID uuid = UUID.randomUUID();

        while (map.containsKey(uuid))
            uuid = UUID.randomUUID();

        return uuid;
    }

    public static AbstractButton loadButton(String actionValue, ItemStack itemStack, Object correspondantManager, GuiManager guiManager, Map<String, String> map, String... args) {
        TriFunction<ItemStack, Object, GuiManager, String[], Map<String, String>, AbstractButton> factory = buttonMap.get(actionValue);
        return factory == null ? null : factory.apply(itemStack, correspondantManager, guiManager, args, map);
    }

    public static Integer getIntegerOrDefault(String s, int def){
        Integer val = getInteger(s);

        return val != null ? val : def;
    }

    public static Integer getInteger(String s){
        try{
            return Integer.parseInt(s);
        }
        catch (Throwable ignored){
            return null;
        }
    }

    public static Double getDoubleOrDefault(String s, double def){
        Double val = getDouble(s);

        return val != null ? val : def;
    }

    public static Double getDouble(String s){
        try{
            return Double.parseDouble(s);
        }
        catch (Throwable ignored){
            return null;
        }
    }

    public static String retrieveFrom(String ident, String split, String... args){
        if(ident == null || split == null || args == null)
            return " ";

        String prefix = ident + split;
        for(String arg : args) {
            if (!arg.startsWith(prefix))
                continue;

            String[] parts = arg.split(split, 2);
            if(parts.length == 2)
                return parts[1];
        }
        return " ";
    }

    public static String getCellValueAsString(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell) ? cell.getDateCellValue().toString() : String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

    public static Set<Integer> getSlots(String from){
        Set<Integer> slots = new HashSet<>();

        if(from == null)
            return slots;

        String[] possibleSlots = from.trim().split(",");

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
