package com.deadshotmdf.GLC_GUIS.General.Managers;

import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.*;
import com.deadshotmdf.GLC_GUIS.General.GUI.PerPlayerGUI;
import com.deadshotmdf.GLC_GUIS.General.GUI.SharedGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractGUIManager {

    protected final GuiManager guiManager;
    protected final JavaPlugin plugin;
    protected final File basePath;

    public AbstractGUIManager(GuiManager guiManager, JavaPlugin plugin, File basePath) {
        this.guiManager = guiManager;
        this.plugin = plugin;
        this.basePath = basePath;
        this.guiManager.addManager(this);
    }

    public void loadGUIsRecursive(){
        this.loadGUIsRecursive(basePath);
    }

    public void loadGUIsRecursive(File directory) {
        if(!basePath.exists() && basePath.isDirectory())
            return;

        File[] files = directory.listFiles();

        if (files == null)
            return;

        for (File file : files) {
            if (file.isDirectory())
                loadGUIsRecursive(file);

            else if (file.isFile() && file.getName().endsWith(".yml"))
                loadGUI(file);
        }
    }

    private void loadGUI(File guiFile) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(guiFile);
        ConfigurationSection guiSection = config.getConfigurationSection("gui");

        if (guiSection == null) {
            plugin.getLogger().warning("No 'gui' section found in " + guiFile.getName());
            return;
        }

        String guiName = guiSection.getString("name");
        if (guiName == null || guiName.isEmpty()) {
            plugin.getLogger().warning("GUI name not defined in " + guiFile.getName());
            return;
        }

        String title = ChatColor.translateAlternateColorCodes('&', guiSection.getString("title", "Default Title"));
        int size = guiSection.getInt("size", 27);
        boolean perPlayer = guiSection.getBoolean("per_player", false);

        //confusing part

        // Parse default elements
        Map<Integer, GuiElement> defaultElements = parseElements(guiSection.getConfigurationSection("default_elements"));

        // Parse page-specific elements
        Map<Integer, Map<Integer, GuiElement>> pages = parsePages(guiSection.getConfigurationSection("pages"));

        System.out.println(defaultElements.size() + " " + pages.size());
        pages.values().forEach(System.out::println);

        // Merge default elements with page-specific elements
        Map<Integer, Map<Integer, GuiElement>> mergedPages = mergeDefaultWithPages(defaultElements, pages);

        if(mergedPages.isEmpty())
            mergedPages.put(0, defaultElements);

        // Register GUI with GuiManager
        guiManager.registerGuiTemplate(guiName.toLowerCase(), perPlayer ? new PerPlayerGUI(guiManager, title, size, mergedPages, null) : new SharedGUI(guiManager, title, size, mergedPages, null));

        plugin.getLogger().info("Loaded GUI: " + guiName + " " + mergedPages.size() + " " + mergedPages.get(0).size());
    }

    private Map<Integer, Map<Integer, GuiElement>> mergeDefaultWithPages(Map<Integer, GuiElement> defaultElements,
                                                                         Map<Integer, Map<Integer, GuiElement>> pages) {

        Map<Integer, Map<Integer, GuiElement>> mergedPages = new LinkedHashMap<>(pages);

        for (Integer page : pages.keySet()) {
            mergedPages.computeIfAbsent(page, _ -> new LinkedHashMap<>());

            for (Map.Entry<Integer, GuiElement> defaultEntry : defaultElements.entrySet()) {
                Integer slot = defaultEntry.getKey();
                GuiElement defaultElement = defaultEntry.getValue();

                GuiElement pageSpecificElement = pages.get(page).get(slot);

                mergedPages.get(page).put(slot, pageSpecificElement != null ? pageSpecificElement : defaultElement);
            }
        }

        return mergedPages;
    }

    private GuiElement parseGuiElement(ConfigurationSection section) {
        if (section == null)
            return null;

        ConfigurationSection itemSection = section.getConfigurationSection("item");
        if (itemSection == null)
            return null;

        boolean purchasableItem = section.getBoolean("purchasableItem", false);
        ItemStack item = purchasableItem ? new ItemStack(Material.DIRT) : parseItem(itemSection);

        if (item == null)
            return null;

        List<String> actions = section.getStringList("actions");
        if (actions.isEmpty())
            return null;

        String action = actions.get(0).toUpperCase();

        if(purchasableItem)
            return new PurchasableButton(item, this);

        if (action.startsWith("OPEN_GUI")) {
            String[] parts = action.split(" ");
            if (parts.length < 2) return null;
            String targetGui = parts[1];
            return new OpenGUIButton(item, this, guiManager, targetGui);
        }

        else if(action.startsWith("BACK_PAGE"))
            return new MovePageButton(item, this,false);

        else if(action.startsWith("NEXT_PAGE"))
            return new MovePageButton(item, this,true);

        else if (action.equalsIgnoreCase("FILLER"))
            return new Filler(item);


        return new Label(item);
    }

    protected ItemStack parseItem(ConfigurationSection itemSection) {
        if (itemSection == null)
            return null;

        String materialName = itemSection.getString("material", "STONE").toUpperCase();
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            plugin.getLogger().warning("Invalid material: " + materialName + ". Defaulting to STONE.");
            material = Material.STONE;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta == null)
            return item;

        String name = itemSection.getString("name", "");
        if (!name.isEmpty())
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        List<String> lore = itemSection.getStringList("lore");
        if (!lore.isEmpty())
            meta.setLore(lore.stream()
                    .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                    .collect(Collectors.toList()));

        item.setItemMeta(meta);

        return item;
    }

    private Map<Integer, GuiElement> parseElements(ConfigurationSection section){
        LinkedHashMap<Integer, GuiElement> elements = new LinkedHashMap<>();

        if(section == null)
            return elements;

        for(String key : section.getKeys(false)){
            Set<Integer> slots = GUIUtils.getSlots(section.getString(key + ".slots"));

            if(slots.isEmpty())
                slots = GUIUtils.getSlots(section.getString(key + ".slot"));

            if(slots.isEmpty())
                continue;

            GuiElement element = parseGuiElement(section.getConfigurationSection(key));

            if(element != null)
                slots.forEach(slot -> elements.put(slot, element));
        }

        return elements;
    }

    private Map<Integer, Map<Integer, GuiElement>> parsePages(ConfigurationSection pagesSection) {
        Map<Integer, Map<Integer, GuiElement>> pages = new LinkedHashMap<>();

        if (pagesSection == null)
            return pages;

        for (String pageKey : pagesSection.getKeys(false)) {
            int pageNumber = Integer.parseInt(pageKey);
            ConfigurationSection pageSection = pagesSection.getConfigurationSection(pageKey + ".elements");
            if (pageSection == null)
                continue;

            Map<Integer, GuiElement> pageElements = parseElements(pageSection);
            pages.put(pageNumber, pageElements);
        }

        return pages;
    }

}
