package com.deadshotmdf.GLC_GUIS.General.Buttons;

import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Filler extends AbstractButton {

    public Filler(ItemStack itemStack) {
        super(itemStack, null);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setHideTooltip(true);
        item.setItemMeta(itemMeta);
    }

    @Override
    public ButtonType getButtonType() {
        return ButtonType.FILLER;
    }

    //Filler doesn't have any name or lore visible so there's no need to keep the parent implementation
    @Override
    public ItemStack getItemStackClone(String[] placeholder, String... replace){ return this.item.clone(); }

    @Override
    public void onClick(InventoryClickEvent event, GUI gui, Object... args) {
        // Do jack shit, fillers are not clickable.
    }
}
