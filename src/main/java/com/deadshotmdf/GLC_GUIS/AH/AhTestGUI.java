package com.deadshotmdf.GLC_GUIS.AH;

import com.deadshotmdf.GLC_GUIS.ActionHouse.ActionHouseManager;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.GUI.PerPlayerGUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class AhTestGUI extends PerPlayerGUI<ActionHouseManager> {

    private final Map<Integer, GuiElement> savedTemplate;

    public AhTestGUI(GuiManager guiManager, ActionHouseManager correspondentManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> pageElements, GUI backGUI, String... args) {
        super(guiManager, correspondentManager, title, size, pageElements, backGUI, args);
        this.savedTemplate = pageElements.get(0);
        this.backGUI = null;
    }

    @Override
    protected GUI createNewInstance(UUID uuid, GUI backGUI, String... args) {
        return new AhTestGUI(guiManager, correspondentManager, title, size, pageElements, backGUI, args);
    }

    @Override
    public void refreshInventory() {
        this.pageElements.clear();
        this.pageElements.put(0, savedTemplate);
        Inventory first = pageInventories.get(0);

        //Maybe concurrentmodificationexception? Shouldn't happen but not risking shit
        try{pageInventories.values().forEach(Inventory::clear);}
        catch (Throwable e){e.printStackTrace();}

        this.pageInventories.clear();
        this.pageInventories.put(0, first != null ? first : (first = Bukkit.createInventory(null, size, title)));
        first.clear();

        //Map<UUID, Transaction> in the manager
//        LinkedHashMap<UUID, Transaction> transactions = correspondentManager.getTransactions();
//
//        int currentPage = 0;
//        Map<Integer, GuiElement> elements = pageElements.get(currentPage);
//
//        if (elements == null)
//            return;
//
//        List<Integer> emptySlots = getEmptySlots(elements);
//        Iterator<Transaction> transactionIterator = transactions.values().iterator();
//        while (transactionIterator.hasNext()) {
//            for (int slot : emptySlots) {
//                if (!transactionIterator.hasNext())
//                    break;
//
//                Transaction transaction = transactionIterator.next();
//
//                if(!transaction.isStillValid())
//                    return;
//
//                elements.put(slot, new AHTransactionButton(transaction.getItem(), correspondentManager, guiManager, null, null, transaction.getSellAmount(), transaction.getGLCoinsSellAmount()));
//            }
//
//            if (!transactionIterator.hasNext())
//                break;
//
//            pageInventories.computeIfAbsent(++currentPage, _ -> Bukkit.createInventory(null, size, title));
//            elements = pageElements.computeIfAbsent(currentPage, _ -> new HashMap<>());
//            elements.putAll(savedTemplate);
//        }
//
//        redirectFolk();
//        super.refreshInventory();
    }

    private List<Integer> getEmptySlots(Map<Integer, GuiElement> elements) {
        List<Integer> emptySlots = new LinkedList<>();
        for (int i = 0; i < size; i++)
            if (!elements.containsKey(i))
                emptySlots.add(i);

        return emptySlots;
    }

    private void redirectFolk(){
        new HashSet<>(this.pageInventories.values()).forEach(inv -> new HashSet<>(inv.getViewers()).forEach(player ->{
            if(getPageByInventory((Player) player) < 0)
                open(player, 0, false);
        }));
    }
}
