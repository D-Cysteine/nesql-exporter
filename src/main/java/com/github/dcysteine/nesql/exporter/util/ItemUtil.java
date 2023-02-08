package com.github.dcysteine.nesql.exporter.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Utility class containing methods for Minecraft items. */
public final class ItemUtil {
    // Static class.
    private ItemUtil() {}

    public static int getItemId(ItemStack itemStack) {
        return Item.getIdFromItem(itemStack.getItem());
    }

    public static Optional<ItemStack> getItemStack(Block block) {
        return Optional.ofNullable(Item.getItemFromBlock(block))
                .map(item -> new ItemStack(Item.getItemFromBlock(block), 1));
    }

    public static Optional<ItemStack> getItemStack(Fluid fluid) {
        return Optional.ofNullable(fluid.getBlock()).flatMap(ItemUtil::getItemStack);
    }

    public static boolean hasWildcardItemDamage(ItemStack itemStack) {
        return itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE;
    }

    /** Returns a map of tool class to harvest level. */
    public static Map<String, Integer> getToolClasses(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item.getToolClasses(itemStack).stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                toolClass -> item.getHarvestLevel(itemStack, toolClass)));
    }
}
