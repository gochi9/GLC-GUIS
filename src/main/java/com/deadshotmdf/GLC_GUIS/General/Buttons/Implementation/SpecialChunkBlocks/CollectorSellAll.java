package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.SpecialChunkBlocks;

import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.ChunkHopper;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocksManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@ButtonIdentifier("COLLECTOR_SELL_ALL")
public class CollectorSellAll extends AbstractButton {

    private final SpecialBlocksManager specialBlocksManager;

    public CollectorSellAll(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
        this.specialBlocksManager = (SpecialBlocksManager) correspondentManager;
    }

    @Override
    public void onClick(InventoryClickEvent event, GUI gui, Object... args) {
        if(args.length < 1 || !(args[1] instanceof ChunkHopper chunkHopper))
            return;

        specialBlocksManager.sellAll(chunkHopper, (Player) event.getWhoClicked(), true, gui);
    }
}
