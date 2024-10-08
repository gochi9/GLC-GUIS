package com.deadshotmdf.GLC_GUIS.BlackMarket.Objects;

import org.bukkit.inventory.ItemStack;

public record BlackmarketPair(ItemStack itemStack, double min_price, double max_price) {
}
