package com.github.dcysteine.nesql.exporter.util;

import betterquesting.api.utils.UuidConverter;
import com.github.dcysteine.nesql.exporter.common.MobSpec;
import com.github.dcysteine.nesql.exporter.render.Renderer;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

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
        String itemId = itemId(itemStack);

        // Split on the first occurrence of ID_SEPARATOR to get the mod name as a separate folder.
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
        String fluidId = fluidId(fluidStack.getFluid());

        // Split on the first occurrence of ID_SEPARATOR to get the mod name as a separate folder.
        int firstIndex = fluidId.indexOf(ID_SEPARATOR);
        return "fluid" + File.separator + fluidId.substring(0, firstIndex) + File.separator
                + fluidId.substring(firstIndex + ID_SEPARATOR.length())
                + Renderer.IMAGE_FILE_EXTENSION;
    }

    public static String mobId(MobSpec spec) {
        String id = spec.getModId() + ID_SEPARATOR + spec.getShortName();

        Optional<NBTTagCompound> nbt = spec.getNbt();
        if (nbt.isPresent()) {
            id += ID_SEPARATOR + StringUtil.encodeNbt(nbt.get());
        }

        return id;
    }

    public static String mobImageFilePath(MobSpec spec) {
        String mobId = mobId(spec);

        // Split on the first occurrence of ID_SEPARATOR to get the mod name as a separate folder.
        int firstIndex = mobId.indexOf(ID_SEPARATOR);
        return "mob" + File.separator + mobId.substring(0, firstIndex) + File.separator
                + mobId.substring(firstIndex + ID_SEPARATOR.length())
                + Renderer.IMAGE_FILE_EXTENSION;
    }

    public static String questLineEntryId(UUID questLineId, UUID questId) {
        return UuidConverter.encodeUuid(questLineId)
                + ID_SEPARATOR + UuidConverter.encodeUuid(questId);
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
