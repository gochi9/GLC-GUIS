package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation;

import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.GLC_GUIS.Shop.GenericShopTransactionGUI;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@ButtonIdentifier("GENERIC_SHOP_CHANGE_AMOUNT")
public class GenericShopChangeAmount extends AbstractButton {

    private final boolean add;
    private final int value;

    public GenericShopChangeAmount(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
        this.add = args.length > 0 && args[0].equalsIgnoreCase("ADD");
        this.value = args.length > 1 ? GUIUtils.getIntegerOrDefault(args[1], 0) : 0;
    }

    public int getValue(){
        return value;
    }

    public boolean isAdd(){
        return add;
    }

    @Override
    public void onClick(InventoryClickEvent event, GUI gui, Object... args) {
        if(value == 0 || !(gui instanceof GenericShopTransactionGUI shop))
            return;

        shop.changeAmount(value, add);
    }
}
