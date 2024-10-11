package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.GUI;

import com.deadshotmdf.GLC_GUIS.ConfigSettings;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.SpecialChunkBlocks.RemoveLoader;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.GUI.PerPlayerGUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocks.ChunkLoader;
import com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.SpecialBlocksManager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoaderGUI extends PerPlayerGUI<SpecialBlocksManager> {

    private final ChunkLoader chunkLoader;

    public LoaderGUI(GuiManager guiManager, SpecialBlocksManager correspondentManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> pageElements, GUI backGUI, UUID viewer, Object... args) {
        super(guiManager, correspondentManager, title, size, pageElements, backGUI, viewer, args);

        this.chunkLoader = args.length > 1 ? (ChunkLoader) args[1] : null;

        if(chunkLoader != null)
            refreshInventory();
    }

    @Override
    protected GUI createNewInstance(UUID player, GUI backGUI, Object... args) {
        return new LoaderGUI(guiManager, correspondentManager, title, size, new HashMap<>(pageElements), backGUI, player, args);
    }

    @Override
    public void handleClick(InventoryClickEvent ev, Object... args) {
        super.handleClick(ev, this.args);
    }

    @Override
    public void refreshInventory(){
        super.refreshInventory();
        Inventory inv = pageInventories.get(0);

        pageElements.get(0).forEach((k, v) -> {
            if(v instanceof RemoveLoader removeLoader)
                inv.setItem(k, removeLoader.getItemStackClone(SpecialBlocksManager.loaderPlaceholers, ConfigSettings.formatDate(chunkLoader.getRawCooldown())));
        });
    }

}
