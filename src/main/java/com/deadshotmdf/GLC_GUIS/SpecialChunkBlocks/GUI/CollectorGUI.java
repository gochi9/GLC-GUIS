package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.GUI;

import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.SpecialChunkBlocks.CollectorItemDisplay;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.GUI.PerPlayerPagedGUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.ChunkHopper;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocksManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CollectorGUI extends PerPlayerPagedGUI<SpecialBlocksManager, Map.Entry<Material, Double>> {

    private final ChunkHopper chunkHopper;

    public CollectorGUI(GuiManager guiManager, SpecialBlocksManager correspondentManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> pageElements, GUI backGUI, UUID viewer, Object... args) {
        super(guiManager, correspondentManager, title, size, pageElements, backGUI, viewer, args);
        this.chunkHopper = args.length > 0 ? (ChunkHopper) args[0] : null;

        if(this.chunkHopper != null && this.viewer != null)
            refreshInventory();
    }

    @Override
    protected GUI createNewInstance(UUID player, GUI backGUI, Object... args) {
        return new CollectorGUI(guiManager, correspondentManager, title, size, pageElements, backGUI, player, args);
    }

    @Override
    protected List<Map.Entry<Material, Double>> getItemsToDisplay() {
        return new ArrayList<>(correspondentManager.getPrices().entrySet());
    }

    @Override
    protected GuiElement createGuiElement(Map.Entry<Material, Double> entry) {
        Material material = entry.getKey();
        int amount = chunkHopper.getValues().getOrDefault(material, 0);
        int maxAmount = GUIUtils.getHighestPermissionNumber(Bukkit.getPlayer(viewer), "glcguis.chunkcollectorcollectsize.");

        CollectorItemDisplay collectorItemDisplay = new CollectorItemDisplay(new ItemStack(material), correspondentManager, guiManager, null, null);
        collectorItemDisplay.setInfo(material, amount, maxAmount, entry.getValue() * amount);
        return collectorItemDisplay;
    }
}
