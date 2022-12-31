package com.github.dcysteine.nesql.exporter.util;

import com.github.dcysteine.nesql.exporter.util.render.Renderer;
import com.google.protobuf.Message;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/** Utility class containing methods for generating unique row IDs. */
public final class IdUtil {
    // Static class.
    private IdUtil() {}

    /** This string needs to be URL parameter-safe, as well as file system-safe. */
    private static final String ID_SEPARATOR = "~";

    public static String itemId(ItemStack itemStack) {
        String id = itemId(itemStack.getItem());
        id += ID_SEPARATOR + itemStack.getItemDamage();

        if (itemStack.hasTagCompound()) {
            id += ID_SEPARATOR + encodeNbt(itemStack.getTagCompound());
        }

        return id;
    }

    public static String itemId(Item item) {
        GameRegistry.UniqueIdentifier uniqueId = GameRegistry.findUniqueIdentifierFor(item);
        return uniqueId + ID_SEPARATOR + Item.getIdFromItem(item);
    }

    public static String modId(ItemStack itemStack) {
        return modId(itemStack.getItem());
    }

    public static String modId(Item item) {
        GameRegistry.UniqueIdentifier uniqueId = GameRegistry.findUniqueIdentifierFor(item);
        return uniqueId.modId;
    }

    public static String imageFilePath(ItemStack itemStack) {
        // The ':' character is not valid on Windows file systems.
        return itemId(itemStack).replace(':', File.separatorChar) + Renderer.IMAGE_FILE_EXTENSION;
    }

    public static String fluidId(FluidStack fluidStack) {
        String id = fluidId(fluidStack.getFluid());

        if (fluidStack.tag != null) {
            id += ID_SEPARATOR + encodeNbt(fluidStack.tag);
        }

        return id;
    }

    public static String fluidId(Fluid fluid) {
        return fluid.getName() + ID_SEPARATOR + fluid.getID();
    }

    public static String imageFilePath(FluidStack fluidStack) {
        return fluidId(fluidStack.getFluid()) + Renderer.IMAGE_FILE_EXTENSION;
    }

    public static String encodeNbt(NBTTagCompound nbt) {
        return Base64.getUrlEncoder().encodeToString(
                nbt.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static String encodeProto(Message proto) {
        return Base64.getUrlEncoder().encodeToString(proto.toByteArray());
    }
}
