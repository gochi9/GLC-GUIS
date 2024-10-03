package com.deadshotmdf.GLC_GUIS.General.Managers;

import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Filler;
import com.deadshotmdf.GLC_GUIS.General.Buttons.GuiElement;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Label;
import com.deadshotmdf.GLC_GUIS.General.Buttons.OpenGUIButton;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractGUIManager {

    protected final GuiManager guiManager;
    protected final JavaPlugin plugin;
    protected final File basePath;

    public AbstractGUIManager(GuiManager guiManager, JavaPlugin plugin, File basePath) {
        this.guiManager = guiManager;
        this.plugin = plugin;
        this.basePath = basePath;
    }

    private void loadGUIsRecursive(File directory) {
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

        // Merge default elements with page-specific elements
        Map<Integer, Map<Integer, GuiElement>> mergedPages = mergeDefaultWithPages(defaultElements, pages);

        // Register GUI with GuiManager
        guiManager.registerGuiTemplate(guiName, perPlayer ? new PerPlayerGUI(guiManager, title, size, mergedPages, null) : new SharedGUI(guiManager, title, size, mergedPages, null));

        plugin.getLogger().info("Loaded GUI: " + guiName);
    }



    private Map<Integer, Map<Integer, GuiElement>> mergeDefaultWithPages(Map<Integer, GuiElement> defaultElements,
                                                                         Map<Integer, Map<Integer, GuiElement>> pages) {
        Map<Integer, Map<Integer, GuiElement>> mergedPages = new HashMap<>();

        for (Map.Entry<Integer, Map<Integer, GuiElement>> entry : pages.entrySet()) {
            int pageNumber = entry.getKey();
            Map<Integer, GuiElement> pageElements = new HashMap<>(defaultElements);
            pageElements.putAll(entry.getValue());
            mergedPages.put(pageNumber, pageElements);
        }

        if (mergedPages.isEmpty() && !defaultElements.isEmpty())
            mergedPages.put(0, new HashMap<>(defaultElements));

        return mergedPages;
    }

    private GuiElement parseGuiElement(ConfigurationSection section) {
        if (section == null)
            return null;

        ConfigurationSection itemSection = section.getConfigurationSection("item");
        if (itemSection == null)
            return null;

        ItemStack item = parseItem(itemSection);
        if (item == null)
            return null;

        List<String> actions = section.getStringList("actions");
        if (actions.isEmpty())
            return null;

        String action = actions.get(0).toUpperCase();
        if (action.startsWith("OPEN_GUI")) {
            String[] parts = action.split(" ");
            if (parts.length < 2) return null;
            String targetGui = parts[1];
            return new OpenGUIButton(item, this, guiManager, targetGui);
        }
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
        HashMap<Integer, GuiElement> elements = new HashMap<>();

        if(section == null)
            return elements;

        for(String key : section.getKeys(false)){
            Set<Integer> slots = GUIUtils.getSlots(section.getString("slots"));

            if(slots.isEmpty())
                GUIUtils.getSlots(section.getString("slot"));

            if(slots.isEmpty())
                continue;

            GuiElement element = parseGuiElement(section.getConfigurationSection(key));
            slots.forEach(slot -> elements.put(slot, element));
        }

        return elements;
    }

    private Map<Integer, Map<Integer, GuiElement>> parsePages(ConfigurationSection pagesSection) {
        Map<Integer, Map<Integer, GuiElement>> pages = new HashMap<>();
        if (pagesSection == null) return pages;

        for (String pageKey : pagesSection.getKeys(false)) {
            int pageNumber = Integer.parseInt(pageKey);
            ConfigurationSection pageSection = pagesSection.getConfigurationSection(pageKey).getConfigurationSection("elements");
            if (pageSection == null)
                continue;

            Map<Integer, GuiElement> pageElements = parseElements(pageSection);
            pages.put(pageNumber, pageElements);
        }

        return pages;
    }

}
