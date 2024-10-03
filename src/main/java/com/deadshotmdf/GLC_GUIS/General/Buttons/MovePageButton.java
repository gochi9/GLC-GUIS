package com.deadshotmdf.GLC_GUIS.General.Buttons;

import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MovePageButton extends AbstractButton{

    private final boolean isForward;

    public MovePageButton(@NotNull ItemStack item, Object correspondentManager, boolean isForward) {
        super(item, correspondentManager);
        this.isForward = isForward;
    }

    @Override
    public ButtonType getButtonType() {
        return null;
    }

    @Override
    public void onClick(InventoryClickEvent ev, GUI gui, Object... args) {
        if(!(ev.getWhoClicked() instanceof Player player))
            return;

        int openPage = gui.getPageByInventory(player);

        if(openPage == -1 || (!isForward && openPage == 0) || (isForward && openPage >= gui.getPageCount()))
            return;

        gui.open(player, isForward ? ++openPage : --openPage);
    }
}
