package com.deadshotmdf.GLC_GUIS.AH.Objects;

import com.deadshotmdf.GLC_GUIS.AH.AHManager;
import com.deadshotmdf.GLC_GUIS.AH.AHTransaction;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.AH.AHPlayerStashRetrive;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.AH.AHSort;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.AH.AHTransactionButton;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.GUI.PerPlayerGUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class AhSharedGUI extends PerPlayerGUI<AHManager> {

    private final Map<Integer, GuiElement> savedTemplate;
    private SortType currentSortType = SortType.NEWEST_ITEMS;

    public AhSharedGUI(GuiManager guiManager, AHManager correspondentManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> pageElements, GUI
            backGUI, UUID viewer, String... args) {
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
    public void refreshInventory() {
        savedTemplate.values().forEach(guiElement -> {
            if(guiElement instanceof AHSort ahSort)
                ahSort.updateLore(this);
        });
        this.pageElements.clear();
        this.pageElements.put(0, new HashMap<>(savedTemplate));
        Inventory first = pageInventories.get(0);

        deletePages();

        this.pageInventories.put(0, first != null ? first : (first = Bukkit.createInventory(null, size, title)));
        first.clear();

        boolean isMain = isMainAHGUI();
        List<AHTransaction> transactions = new ArrayList<>(isMain ? correspondentManager.getTransactions().values() : correspondentManager.getPlayerStash(viewer));

        int currentPage = 0;
        Map<Integer, GuiElement> elements = pageElements.get(currentPage);

        if (elements == null)
            return;

        List<Integer> emptySlots = getEmptySlots(elements);
        transactions.removeIf(transaction -> (isMain && !transaction.isStillValid()) || (!isMain && !transaction.doesExist()));

        SortType sortType = getCurrentSortType();

        Comparator<AHTransaction> comparator = getComparator(sortType);
        if (comparator != null)
            transactions.sort(comparator);

        Iterator<AHTransaction> transactionIterator = transactions.iterator();
        while (transactionIterator.hasNext()) {
            for (int slot : emptySlots) {
                if (!transactionIterator.hasNext())
                    break;

                AHTransaction transaction = transactionIterator.next();

                if (transaction == null)
                    continue;

                ItemStack item = transaction.getItem().clone();
                Map<String, String> info = Map.of("transaction_id", transaction.getTransactionID().toString(), "viewer", viewer.toString());
                GuiElement guiElement = isMain ? new AHTransactionButton(item, correspondentManager, guiManager, null, info) : new AHPlayerStashRetrive(item, correspondentManager, guiManager, null, info);

                if (guiElement instanceof AHPlayerStashRetrive button)
                    button.setTransaction(transaction);

                elements.put(slot, guiElement);
            }

            if (!transactionIterator.hasNext())
                break;

            pageInventories.computeIfAbsent(++currentPage, _ -> Bukkit.createInventory(null, size, title));
            elements = pageElements.computeIfAbsent(currentPage, _ -> new HashMap<>());
            elements.putAll(savedTemplate);
            emptySlots = getEmptySlots(elements);
        }

        int finalPage = currentPage;
        pageInventories.entrySet().removeIf(k -> k.getKey() > finalPage);
        super.refreshInventory();
        redirectFolk();
        updateTitle(getPageCount());
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

    private List<Integer> getEmptySlots(Map<Integer, GuiElement> elements) {
        List<Integer> emptySlots = new LinkedList<>();
        for (int i = 0; i < size; i++)
            if (!elements.containsKey(i))
                emptySlots.add(i);

        return emptySlots;
    }

    private void redirectFolk() {
        if (viewer == null)
            return;

        Player player = Bukkit.getPlayer(viewer);

        if (player != null && player.isOnline() && getPageByInventory(player) < 0)
            open(player, 0, false);
    }
}
