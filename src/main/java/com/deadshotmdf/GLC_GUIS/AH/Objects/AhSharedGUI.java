package com.deadshotmdf.GLC_GUIS.AH.Objects;

import com.deadshotmdf.GLC_GUIS.AH.AHManager;
import com.deadshotmdf.GLC_GUIS.AH.AHTransaction;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.AH.AHPlayerStashRetrive;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.AH.AHSort;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.AH.AHTransactionButton;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.GUI.PerPlayerPagedGUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class AhSharedGUI extends PerPlayerPagedGUI<AHManager, AHTransaction> {

    private final Map<Integer, GuiElement> savedTemplate;
    private SortType currentSortType = SortType.NEWEST_ITEMS;

    public AhSharedGUI(GuiManager guiManager, AHManager correspondentManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> pageElements, GUI
            backGUI, UUID viewer, Object... args) {
        super(guiManager, correspondentManager, title, size, pageElements, backGUI, viewer, args);
        this.savedTemplate = new HashMap<>(pageElements.get(0));
        this.backGUI = null;

        if (this.viewer != null)
            this.refreshInventory();
    }

    public abstract boolean isMainAHGUI();

    public void setCurrentSortType() {
        this.currentSortType = currentSortType.getNext();
        refreshInventory();
    }

    public SortType getCurrentSortType() {
        return currentSortType;
    }

    @Override
    protected void beforeRefreshInventory() {
        savedTemplate.values().forEach(guiElement -> {
            if (guiElement instanceof AHSort ahSort)
                ahSort.updateLore(this);
        });
    }

    @Override
    protected List<AHTransaction> getItemsToDisplay() {
        boolean isMain = isMainAHGUI();
        List<AHTransaction> transactions = new ArrayList<>(isMain ? correspondentManager.getTransactions().values() : correspondentManager.getPlayerStash(viewer));

        transactions.removeIf(transaction -> (isMain && !transaction.isStillValid()) || (!isMain && !transaction.doesExist()));

        Comparator<AHTransaction> comparator = getComparator(currentSortType);
        if (comparator != null)
            transactions.sort(comparator);

        return transactions;
    }

    @Override
    protected GuiElement createGuiElement(AHTransaction transaction) {
        boolean isMain = isMainAHGUI();

        ItemStack item = transaction.getItem().clone();
        Map<String, String> info = Map.of("transaction_id", transaction.getTransactionID().toString(), "viewer", viewer.toString());

        GuiElement guiElement = isMain ? new AHTransactionButton(item, correspondentManager, guiManager, null, info) : new AHPlayerStashRetrive(item, correspondentManager, guiManager, null, info);

        if (guiElement instanceof AHPlayerStashRetrive button)
            button.setTransaction(transaction);

        return guiElement;
    }

    @Override
    protected void afterRefreshInventory() {
        redirectFolk();
    }

    private Comparator<AHTransaction> getComparator(SortType sortType) {
        return switch (sortType) {
            case PRICE_MAX ->
                    Comparator.<AHTransaction>comparingDouble(t -> t.getSellAmount() == 0.000 ? Double.NEGATIVE_INFINITY : t.getSellAmount()).reversed();
            case PRICE_MIN ->
                    Comparator.comparingDouble(t -> t.getSellAmount() == 0.000 ? Double.POSITIVE_INFINITY : t.getSellAmount());
            case GLCOINS_PRICE_MAX ->
                    Comparator.<AHTransaction>comparingDouble(t -> t.getGLCoinsSellAmount() == 0.000 ? Double.NEGATIVE_INFINITY : t.getGLCoinsSellAmount()).reversed();
            case GLCOINS_PRICE_MIN ->
                    Comparator.comparingDouble(t -> t.getGLCoinsSellAmount() == 0.000 ? Double.POSITIVE_INFINITY : t.getGLCoinsSellAmount());
            case NEWEST_ITEMS -> Comparator.comparingLong(AHTransaction::getExpire).reversed();
            case OLDEST_ITEMS -> Comparator.comparingLong(AHTransaction::getExpire);
        };
    }

    private void redirectFolk() {
        if (viewer == null)
            return;

        Player player = Bukkit.getPlayer(viewer);

        if (player != null && player.isOnline() && getPageByInventory(player) < 0)
            open(player, 0, false);
    }
}
