package com.deadshotmdf.GLC_GUIS.General.Managers;

import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.*;
import com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation.Label;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.GUI.GuiElementsData;
import com.deadshotmdf.GLC_GUIS.General.GUI.PerPlayerGUI;
import com.deadshotmdf.GLC_GUIS.General.GUI.SharedGUI;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
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
                loadGUI(file, file.getName().replace(".yml", ""));
        }
    }

    private void loadGUI(File guiFile, String guiName) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(guiFile);
        ConfigurationSection guiSection = config.getConfigurationSection("gui");

        if (guiSection == null) {
            plugin.getLogger().warning("No 'gui' section found in " + guiFile.getName());
            return;
        }

        String title = ChatColor.translateAlternateColorCodes('&', guiSection.getString("title", "Default Title"));
        int size = guiSection.getInt("size", 27);
        boolean perPlayer = guiSection.getBoolean("per_player", false);

        String excelFileName = guiSection.getString("excel_file_name");
        if (excelFileName == null || excelFileName.isEmpty()) {
            plugin.getLogger().warning("Excel file name not defined in " + guiFile.getName());
            return;
        }

        File excelFile = new File(guiFile.getParentFile(), excelFileName);
        if (!excelFile.exists()) {
            plugin.getLogger().info("Excel file " + excelFileName + " does not exist. Creating default Excel file.");
            String defaultFormat = guiSection.getString("default_format");
            if (defaultFormat == null || defaultFormat.isEmpty()) {
                plugin.getLogger().warning("Default format not defined in " + guiFile.getName() + ". Skipping GUI.");
                return;
            }

            createDefaultExcelFile(excelFile, defaultFormat);
        }

        GuiElementsData guiElementsData = parseExcelFile(excelFile);

        Map<Integer, Map<Integer, GuiElement>> mergedPages = mergeDefaultWithPages(
                guiElementsData.getDefaultElements(),
                guiElementsData.getPages()
        );

        if(mergedPages.isEmpty())
            mergedPages.put(0, guiElementsData.getDefaultElements());

        guiManager.registerGuiTemplate(guiName.toLowerCase(), specifyGUI(perPlayer, guiManager, title, size, mergedPages, config.getString("specialType")));

        plugin.getLogger().info("Loaded GUI: " + guiName + " " + mergedPages.size() + " " + mergedPages.get(0).size());
    }

    private void createDefaultExcelFile(File excelFile, String defaultFormat) {
        String[] headers = defaultFormat.split(",");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("GUI Elements");

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i].trim());
        }

        try (FileOutputStream fos = new FileOutputStream(excelFile)) {
            workbook.write(fos);
        }
        catch (IOException e) {
            plugin.getLogger().severe("Error creating Excel file: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            try {
                workbook.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<Integer, Map<Integer, GuiElement>> mergeDefaultWithPages(Map<Integer, GuiElement> defaultElements, Map<Integer, Map<Integer, GuiElement>> pages) {
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

    private GuiElementsData parseExcelFile(File excelFile) {
        Map<Integer, GuiElement> defaultElements = new LinkedHashMap<>();
        Map<Integer, Map<Integer, GuiElement>> pages = new LinkedHashMap<>();

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (!rowIterator.hasNext()) {
                plugin.getLogger().warning("Excel file " + excelFile.getName() + " is empty.");
                return new GuiElementsData(defaultElements, pages);
            }

            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow)
                headers.add(cell.getStringCellValue().trim());

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, String> elementData = new HashMap<>();

                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String value = GUIUtils.getCellValueAsString(cell);
                    elementData.put(headers.get(i), value);
                }

                GuiElement element = createGuiElementFromData(elementData);
                if (element == null)
                    continue;

                int page = parsePageNumber(elementData.get("page"));
                Set<Integer> slots = GUIUtils.getSlots(elementData.get("slots"));

                if(slots.isEmpty())
                    slots = GUIUtils.getSlots(elementData.get("slot"));

                if (slots.isEmpty())
                    continue;

                slots.forEach(slot ->{
                    if(page < 0)
                        defaultElements.put(slot, element);
                    else
                        pages.computeIfAbsent(page, k -> new LinkedHashMap<>()).put(slot, element);
                });
            }

        }
        catch (Throwable e) {
            plugin.getLogger().severe("Error reading Excel file: " + e.getMessage());
            e.printStackTrace();
        }

        return new GuiElementsData(defaultElements, pages);
    }

    private int parsePageNumber(String pageStr) {
        if (pageStr == null || pageStr.isEmpty() || pageStr.equals("-"))
            return -1;
        try {
            int page = Integer.parseInt(pageStr);
            return page >= 0 ? page : -1;
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }

    private GuiElement createGuiElementFromData(Map<String, String> elementData) {
        ItemStack item = parseItem(elementData);

        if (item == null)
            return null;

        String actionStr = elementData.get("action");
        if (actionStr == null || actionStr.isEmpty())
            return null;

        String[] actionParts = actionStr.split("\\s+");
        String actionName = actionParts[0].toUpperCase();
        String[] args = Arrays.copyOfRange(actionParts, 1, actionParts.length);

        Map<String, String> extraValues = new HashMap<>(elementData);
        extraValues.keySet().removeAll(Arrays.asList("page", "slot", "material", "name", "lore", "action"));
        AbstractButton button = GUIUtils.loadButton(actionName, item, this, guiManager, extraValues, args);
        if (button == null)
            return new Label(item, this, guiManager, args, extraValues);

        enhanceGuiElement(extraValues, button, actionName, args);
        return button;
    }

    //Override this method to retrieve specific information from a certain type of GUI for specific cases
    protected GuiElement enhanceGuiElement(Map<String, String> extraValues, GuiElement element, String action, String[] args) {
        return element;
    }

    protected GUI specifyGUI(boolean perPlayer, GuiManager guiManager, String title, int size, Map<Integer, Map<Integer, GuiElement>> mergedPages, String type){
        return perPlayer ? new PerPlayerGUI(guiManager, title, size, mergedPages, null) : new SharedGUI(guiManager, title, size, mergedPages, null);
    }

    private ItemStack parseItem(Map<String, String> elementData) {
        String materialName = elementData.getOrDefault("material", "STONE").toUpperCase();
        Material material = Material.getMaterial(materialName);

        if (material == null) {
            plugin.getLogger().warning("Invalid material: " + materialName);
            return null;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta == null)
            return item;

        String name = elementData.get("name");
        if (name != null && !name.isEmpty())
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        String loreStr = elementData.get("lore");
        if (loreStr != null && !loreStr.isEmpty())
            meta.setLore(Arrays.stream(loreStr.split("\\n|\\|"))
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                    .collect(Collectors.toList()));

        item.setItemMeta(meta);
        return item;
    }

}
