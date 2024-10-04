package com.deadshotmdf.GLC_GUIS.General.Buttons.Implementation;

import com.deadshotmdf.GLC_GUIS.GUIUtils;
import com.deadshotmdf.GLC_GUIS.General.Buttons.AbstractButton;
import com.deadshotmdf.GLC_GUIS.General.Buttons.ButtonIdentifier;
import com.deadshotmdf.GLC_GUIS.General.GUI.GUI;
import com.deadshotmdf.GLC_GUIS.General.Managers.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@ButtonIdentifier("GENERIC_SHOP_INTENT")
public class GenericShopIntentButton extends AbstractButton {

    private final String purchaseScreen;
    private final Material material;
    private final double buy_value;
    private final double sell_value;
    private final int max_buy;
    private final int max_sell;

    public GenericShopIntentButton(@NotNull ItemStack item, Object correspondentManager, GuiManager guiManager, String[] args, Map<String, String> elementData) {
        super(item, correspondentManager, guiManager, args, elementData);
        this.purchaseScreen = args.length > 0 ? args[0] : "shop";
        this.material = item.getType();
        this.buy_value = GUIUtils.getDoubleOrDefault(elementData.get("buy_value"), 0);
        this.sell_value = GUIUtils.getDoubleOrDefault(elementData.get("sell_value"), 0);
        this.max_buy = GUIUtils.getIntegerOrDefault(elementData.get("max_buy"), 0);
        this.max_sell = GUIUtils.getIntegerOrDefault(elementData.get("max_sell"), 0);
    }

    @Override
    public void onClick(InventoryClickEvent event, GUI gui, Object... args) {
        if(purchaseScreen == null || purchaseScreen.isBlank() || guiManager.getGuiTemplate(purchaseScreen) == null)
            return;

        boolean isBuy = event.getClick() == ClickType.LEFT;

        if((isBuy && (buy_value <= 0.0 || max_buy <= 0)) || (!isBuy && (sell_value <= 0.0 || max_sell <= 0)))
            return;

        guiManager.openGui((Player) event.getWhoClicked(), purchaseScreen, gui,
                "buy_value:" + buy_value,
                    "sell_value:" + sell_value,
                    "max_buy:" + max_buy,
                    "max_sell:" + max_sell,
                    "material:" + material,
                    "item_name:" + (item.hasItemMeta() ? item.getItemMeta().getDisplayName() : material.toString()),
                    "buying:" + isBuy);
    }
}
