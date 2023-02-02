package com.github.dcysteine.nesql.exporter.plugin.gregtech.util;

import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;

import java.util.List;

public class GregTechUtil {
    // Static class.
    private GregTechUtil() {}

    /** Returns a list of item stacks that unify into the provided item stack. */
    public static List<ItemStack> reverseUnify(ItemStack itemStack) {
        ItemStack unified = GT_OreDictUnificator.get_nocopy(itemStack);
        return GT_OreDictUnificator.getNonUnifiedStacks(unified);
    }

    /** Returns a list of item stacks that unify into the provided object. */
    public static List<ItemStack> reverseUnify(Object object) {
        return GT_OreDictUnificator.getNonUnifiedStacks(object);
    }
}
