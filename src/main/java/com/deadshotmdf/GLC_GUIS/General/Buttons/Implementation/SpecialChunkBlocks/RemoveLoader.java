package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.SpecialChunkBlocks;

import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocksManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@ButtonIdentifier("LOADER_REMOVE")
public class RemoveLoader extends AbstractButton {

    private final SpecialBlocksManager specialBlocksManager;

    public RemoveLoader(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
        this.specialBlocksManager = (SpecialBlocksManager) correspondentManager;
    }

    @Override
    public void onClick(InventoryClickEvent ev, GUI gui, Object... args) {
        if(args.length == 0 || !(args[0] instanceof Location location))
            return;

        Player player = (Player) ev.getWhoClicked();
        ev.getInventory().clear();
        player.closeInventory();
        specialBlocksManager.retakeLoader(player, location);
    }
}
