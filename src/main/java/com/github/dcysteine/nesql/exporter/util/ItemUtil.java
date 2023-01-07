package com.github.dcysteine.nesql.exporter.util;

import com.github.dcysteine.nesql.exporter.main.Logger;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

    public static ItemStack getItemStack(Block block) {
        return new ItemStack(Item.getItemFromBlock(block), 1);
    }

    public static Optional<ItemStack> getItemStack(Fluid fluid) {
        Block block = fluid.getBlock();
        if (block == null) {
            return Optional.empty();
        }

        return Optional.of(getItemStack(block));
    }

    public static boolean isWildcardItem(ItemStack itemStack) {
        return itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE;
    }

    public static boolean isWildcardNbt(NBTTagCompound nbt) {
        if (nbt == null) {
            return false;
        }

        if (nbt.getBoolean("*")) {
            if (!nbt.toString().equals("{*:1b}")) {
                Logger.MOD.error("Found wildcard NBT tag with additional tags:\n{}", nbt);
                return false;
            }
            return true;
        }
        return false;
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
