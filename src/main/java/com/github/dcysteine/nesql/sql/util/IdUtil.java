package com.github.dcysteine.nesql.sql.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/** Utility class containing methods for generating unique row IDs. */
public final class IdUtil {
    /** Static class. */
    private IdUtil() {}

    private static final String ID_SEPARATOR = "~";

    public static String itemId(ItemStack itemStack) {
        String id = itemId(itemStack.getItem());
        id += ID_SEPARATOR + itemStack.getItemDamage();

        if (itemStack.hasTagCompound()) {
            id += ID_SEPARATOR + encodeNbt(itemStack.getTagCompound());
        }

        return id;
    }

    // TODO if not used, inline into above method
    public static String itemId(Item item) {
        return item.getUnlocalizedName() + ID_SEPARATOR + Item.getIdFromItem(item);
    }

    public static String fluidId(FluidStack fluidStack) {
        String id = fluidId(fluidStack.getFluid());

        if (fluidStack.tag != null) {
            id += ID_SEPARATOR + encodeNbt(fluidStack.tag);
        }

        return id;
    }

    // TODO if not used, inline into above method
    public static String fluidId(Fluid fluid) {
        return fluid.getUnlocalizedName() + ID_SEPARATOR + fluid.getID();
    }

    public static String encodeNbt(NBTTagCompound nbt) {
        return Base64.getUrlEncoder().encodeToString(
                nbt.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeNbt(String encodedNbt) {
        return new String(Base64.getUrlDecoder().decode(encodedNbt), StandardCharsets.UTF_8);
    }
}
