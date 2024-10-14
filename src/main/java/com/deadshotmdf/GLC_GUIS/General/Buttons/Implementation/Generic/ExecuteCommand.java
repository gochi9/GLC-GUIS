package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.Generic;

import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.StringJoiner;

@ButtonIdentifier("EXECUTE_COMMAND")
public class ExecuteCommand extends AbstractButton {

    private final String command;

    public ExecuteCommand(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);

        StringJoiner joiner = new StringJoiner(" ");
        for(int i = 0; i < args.length; ++i)
            joiner.add(args[i]);

        this.command = joiner.toString();
    }

    @Override
    public void onClick(InventoryClickEvent ev, GUI gui, Object... args) {
        if(command == null || command.isBlank())
            return;

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", ev.getWhoClicked().getName()));
    }

}
