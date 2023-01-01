package com.github.dcysteine.nesql.exporter.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/** Utility class containing methods for Minecraft items. */
public final class ItemUtil {
    // Static class.
    private ItemUtil() {}

    public static int getItemId(ItemStack itemStack) {
        return Item.getIdFromItem(itemStack.getItem());
    }

    public static boolean isWildcardItem(ItemStack itemStack) {
        return itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE;
    }
}
