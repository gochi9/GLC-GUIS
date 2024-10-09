package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.AH;

import com.deadshotmdf.GLC_GUIS.AH.AHTransaction;
import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AHPlayerStashRetrive extends AbstractButton {

    private AHTransaction transaction;

    public AHPlayerStashRetrive(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
    }

    public void setTransaction(AHTransaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public void onClick(InventoryClickEvent ev, GUI gui, Object... args) {
        if(transaction == null || !transaction.doesExist())
            return;

        ItemStack item = transaction.getItem();

        if(item == null)
            return;

        transaction.setNoLongerExists();
        HumanEntity player = ev.getWhoClicked();
        player.getInventory().addItem(item).values().forEach(remain -> player.getWorld().dropItemNaturally(player.getLocation(), remain));
        gui.refreshInventory();
    }
}
