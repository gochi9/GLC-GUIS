package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.SpecialChunkBlocks;

import com.deadshotmdf.GLC_GUIS.GLCGGUIS;
import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.ChunkHopper;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocksManager;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashMap;
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

        chunkHopper.trimUselessInfo();

        if(chunkHopper.getValues().isEmpty())
            return;

        EnumMap<Material, Double> values = specialBlocksManager.getPrices();
        AtomicDouble total = new AtomicDouble(0.000);
        new HashMap<>(chunkHopper.getValues()).forEach((k, v) ->{
            if(v < 1)
                return;

            double value = values.getOrDefault(k, 0.000);

            if(value <= 0.000)
                return;

            total.set(total.get() + ((v*1d)*value));
        });
        double fin = total.get();

        if(fin <= 0.000)
            return;

        GLCGGUIS.getEconomy().depositPlayer((OfflinePlayer) event.getWhoClicked(), fin);
        chunkHopper.getValues().clear();
        gui.refreshInventory();
    }
}
