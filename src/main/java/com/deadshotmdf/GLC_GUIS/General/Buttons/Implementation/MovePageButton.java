package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation;

import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@ButtonIdentifier("MOVE_PAGE")
public class MovePageButton extends AbstractButton {

    private final boolean isForward;

    public MovePageButton(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
        this.isForward = args.length > 0 && args[0].equalsIgnoreCase("forward");
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
