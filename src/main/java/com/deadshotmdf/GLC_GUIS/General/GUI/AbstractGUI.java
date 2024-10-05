package com.deadshotmdf.GLC_GUIS.General.GUI;

import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Managers.AbstractGUIManager;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractGUI<T extends AbstractGUIManager> implements GUI{

    protected GuiManager guiManager;
    protected final T correspondentManager;
    protected final String title;
    protected final int size;
    protected final Map<Integer, Inventory> pageInventories;
    protected final Map<Integer, Map<Integer, GuiElement>> pageElements;
    protected final int totalPages;
    protected GUI backGUI;
    protected String[] placeholders, replacements;
    private boolean changingPage;
    protected String[] args;

    protected AbstractGUI(GuiManager guiManager, T correspondentManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> pageElements, String... args) {
        this.guiManager = guiManager;
        this.correspondentManager = correspondentManager;
        this.title = title;
        this.size = size;
        this.pageInventories = new HashMap<>();
        this.pageElements = pageElements;
        this.args = args;

        int pages = 0;
        if(pageElements.isEmpty() || pageElements.get(0) == null)
            pageInventories.put(0, Bukkit.createInventory(null, size, title));

        for(Map.Entry<Integer, Map<Integer, GuiElement>> entry : pageElements.entrySet()){
            Inventory inv = Bukkit.createInventory(null, size, title);
            entry.getValue().forEach((slot, element) -> inv.setItem(slot, element.getItemStackClone(null)));
            pageInventories.put(entry.getKey(), inv);
            ++pages;
        }

        this.totalPages = Math.max(1, pages);
    }

    @Override
    public void setBackGUI(GUI backGUI){
        this.backGUI = backGUI;
    }

    @Override
    public GUI getBackGUI(){
        return backGUI;
    }

    @Override
    public abstract boolean isShared();

    @Override
    public GUI createInstance(UUID player, GUI backGUI, String... args){
        return isShared() ? this : createNewInstance(player, backGUI, args);
    }

    protected abstract GUI createNewInstance(UUID player, GUI backGUI, String... args);

    @Override
    public void open(HumanEntity player, int page){
        Inventory inventory = pageInventories.get(page);

        if (page < 0 || page >= totalPages || inventory == null)
            return;

        changingPage = true;
        player.openInventory(inventory);
    }

    @Override
    public void handleClick(InventoryClickEvent ev, Object... args) {
        Inventory inventory = ev.getInventory();
        int page = getPageByInventory(inventory);

        if (page == -1){
            guiManager.removeOpenGui(ev.getWhoClicked(), false);
            return;
        }

        int slot = ev.getRawSlot();
        Map<Integer, GuiElement> elements = pageElements.get(page);
        if (elements == null)
            return;

        GuiElement element = elements.get(slot);
        if (element == null)
            return;

        element.onClick(ev, this, args);
    }

    @Override
    public void handleClose(InventoryCloseEvent ev) {
        if(changingPage){
            changingPage = false;
            return;
        }

        guiManager.removeOpenGui((Player) ev.getPlayer(), true);
    }

    @Override
    public void refreshInventory(){
        for(Map.Entry<Integer, Inventory> inventoryEntry : pageInventories.entrySet()){
            Inventory inventory = inventoryEntry.getValue();

            Map<Integer, GuiElement> elements = pageElements.get(inventoryEntry.getKey());

            if(elements == null)
                continue;

            elements.forEach((slot, element) -> inventory.setItem(slot, element.getItemStackClone(placeholders, replacements)));
        }
    }

    @Override
    public int getPageCount() {
        return pageInventories.size();
    }

    @Override
    public void deletePages(){
        try{pageInventories.values().forEach(Inventory::clear);}
        catch(Throwable ignored){}
    }

    @Override
    public int getPageByInventory(Player player){
        return getPageByInventory(player.getOpenInventory().getTopInventory());
    }

    @Override
    public int getPageByInventory(Inventory inventory) {
        if(inventory == null)
            return -1;

        for (Map.Entry<Integer, Inventory> entry : pageInventories.entrySet()) {
            if (entry.getValue() == inventory)
                return entry.getKey();
        }

        return -1;
    }

}
