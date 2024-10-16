package com.deadshotmdf.GLC_GUIS.Mayor;

import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.Mayor.MayorUpgrade;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.GUI.PerPlayerGUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.UUID;

public class MayorGUI extends PerPlayerGUI<MayorManager> {

    public MayorGUI(GuiManager guiManager, MayorManager correspondentManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> pageElements, GUI backGUI, UUID viewer, Object... args) {
        super(guiManager, correspondentManager, title, size, pageElements, backGUI, viewer, args);

        if(this.viewer != null)
            this.refreshInventory();
    }

    @Override
    public void handleClick(InventoryClickEvent ev, Object... args) {
        if(viewer != null)
            super.handleClick(ev, args);
    }

    @Override
    protected GUI createNewInstance(UUID uuid, GUI backGUI, Object... args) {
        return new MayorGUI(guiManager, correspondentManager, title, size, pageElements, backGUI, uuid, args);
    }

    @Override
    public void refreshInventory(){
        super.refreshInventory();
        Map<UpgradeType, Integer> levels = correspondentManager.getPlayerUpgrades(viewer);
        DelayUpgradePair delayUpgradePair = correspondentManager.getDelayedUpgrade(viewer);

        pageElements.forEach((page, elements) -> {
            Inventory inventory = pageInventories.get(page);

            elements.forEach((slot, element) ->{
                if(element instanceof MayorUpgrade mayorUpgrade && mayorUpgrade.getUpgradeType() != null)
                    inventory.setItem(slot, mayorUpgrade.getMayorItem(correspondentManager.getUpgrade(mayorUpgrade.getUpgradeType()), levels, delayUpgradePair));
            });
        });
    }
}
