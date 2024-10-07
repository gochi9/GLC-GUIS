package com.deadshotmdf.GLC_GUIS.BlackMarket;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public record BlackmarketPair(ItemStack itemStack, double min_price, double max_price, UUID id) {
}
