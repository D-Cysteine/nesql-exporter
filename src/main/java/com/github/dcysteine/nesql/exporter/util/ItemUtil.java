package com.github.dcysteine.nesql.exporter.util;

import com.github.dcysteine.nesql.exporter.main.Logger;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
}
