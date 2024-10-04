package com.deadshotmdf.GLC_GUIS;

import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.CommandIdentifier;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.OpenGUIButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.TriFunction;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Logger;

public class GUIUtils {

    private static final Map<String, TriFunction<ItemStack, Object, GuiManager, String[], AbstractButton>> buttonMap = new HashMap<>();

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

            CommandIdentifier action = button.getAnnotation(CommandIdentifier.class);

            if(action == null)
                continue;

            String actionValue = action.value();

            if(actionValue == null || actionValue.isBlank())
                continue;

            buttonMap.put(actionValue, (itemStack, correspondantManager, guiManager, args) -> {
                try {
                    Constructor<? extends AbstractButton> constructor = button.getConstructor(ItemStack.class, Object.class, GuiManager.class, String[].class);
                    return constructor.newInstance(itemStack, correspondantManager, guiManager, args);
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

    public static AbstractButton loadButton(String actionValue, ItemStack itemStack, Object correspondantManager, GuiManager guiManager, String... args) {
        TriFunction<ItemStack, Object, GuiManager, String[], AbstractButton> factory = buttonMap.get(actionValue);
        return factory == null ? null : factory.apply(itemStack, correspondantManager, guiManager, args);
    }

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
