package com.github.dcysteine.nesql.exporter.util;

import com.github.dcysteine.nesql.exporter.util.render.Renderer;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.io.File;

/** Utility class containing methods for generating unique row IDs. */
public final class IdUtil {
    // Static class.
    private IdUtil() {}

    /** This string needs to be URL parameter-safe, as well as file system-safe. */
    public static final String ID_SEPARATOR = "~";

    public static String itemId(ItemStack itemStack) {
        String id = itemId(itemStack.getItem());
        id += ID_SEPARATOR + itemStack.getItemDamage();

        NBTTagCompound nbt = itemStack.getTagCompound();
        if (nbt != null) {
            id += ID_SEPARATOR + StringUtil.encodeNbt(nbt);
        }

        return id;
    }

    public static String itemId(Item item) {
        GameRegistry.UniqueIdentifier uniqueId = GameRegistry.findUniqueIdentifierFor(item);
        return sanitize(uniqueId.modId + ID_SEPARATOR + uniqueId.name);
    }

    public static String imageFilePath(ItemStack itemStack) {
        // Replace the first occurrence of ID_SEPARATOR to get the mod name as its own separate
        // folder.
        String itemId = itemId(itemStack);
        int firstIndex = itemId.indexOf(ID_SEPARATOR);
        return "item" + File.separator + itemId.substring(0, firstIndex) + File.separator
                + itemId.substring(firstIndex + ID_SEPARATOR.length())
                + Renderer.IMAGE_FILE_EXTENSION;
    }

    public static String fluidId(FluidStack fluidStack) {
        String id = fluidId(fluidStack.getFluid());

        NBTTagCompound nbt = fluidStack.tag;
        if (nbt != null) {
            id += ID_SEPARATOR + StringUtil.encodeNbt(nbt);
        }

        return id;
    }

    public static String fluidId(Fluid fluid) {
        String uniqueName = FluidRegistry.getDefaultFluidName(fluid);
        int separator = uniqueName.indexOf(':');
        return sanitize(
                uniqueName.substring(0, separator)
                        + ID_SEPARATOR + uniqueName.substring(separator + 1));
    }

    public static String imageFilePath(FluidStack fluidStack) {
        // Replace the first occurrence of ID_SEPARATOR to get the mod name as its own separate
        // folder.
        String fluidId = fluidId(fluidStack.getFluid());
        int firstIndex = fluidId.indexOf(ID_SEPARATOR);
        return "fluid" + File.separator + fluidId.substring(0, firstIndex) + File.separator
                + fluidId.substring(firstIndex + ID_SEPARATOR.length())
                + Renderer.IMAGE_FILE_EXTENSION;
    }

    public static String entityId(Entity entity) {
        String id = entityId(EntityList.getEntityString(entity));

        NBTTagCompound nbt = entity.getEntityData();
        if (nbt != null) {
            id += ID_SEPARATOR + StringUtil.encodeNbt(nbt);
        }

        return id;
    }

    public static String entityId(String entityId) {
        int firstIndex = entityId.indexOf('.');
        if (firstIndex > 0)
            return sanitize(entityId.substring(0, firstIndex) + ID_SEPARATOR
                    + entityId.substring(firstIndex + ID_SEPARATOR.length()));
        else
            return sanitize("minecraft" + ID_SEPARATOR + entityId);
    }

    public static String imageFilePath(Entity entity) {
        String entityId = entityId(entity);
        int firstIndex = entityId.indexOf(ID_SEPARATOR);
        return "entity" + File.separator + entityId.substring(0, firstIndex) + File.separator
                + entityId.substring(firstIndex + ID_SEPARATOR.length())
                + Renderer.IMAGE_FILE_EXTENSION;
    }

    /**
     * Strips out URL- and file system-unsafe characters.
     *
     * <p>Windows in particular is a bit finicky. We may need to expand this method in the future.
     * See <a href="https://stackoverflow.com/a/48962674">here</a>.
     */
    public static String sanitize(String string) {
        // Note: four backslashes are needed to escape to a single backslash in the target string.
        return string.replaceAll("[<>:\"/\\\\|?*]", "");
    }
}
