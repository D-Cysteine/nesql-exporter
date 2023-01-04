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
import java.util.Arrays;
import java.util.Base64;
import java.util.zip.Deflater;

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
        return sanitize(
                uniqueId.modId + ID_SEPARATOR + uniqueId.name + ID_SEPARATOR
                        + Item.getIdFromItem(item));
    }

    public static String modId(ItemStack itemStack) {
        return modId(itemStack.getItem());
    }

    public static String modId(Item item) {
        GameRegistry.UniqueIdentifier uniqueId = GameRegistry.findUniqueIdentifierFor(item);
        return sanitize(uniqueId.modId);
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

        if (fluidStack.tag != null) {
            id += ID_SEPARATOR + encodeNbt(fluidStack.tag);
        }

        return id;
    }

    public static String fluidId(Fluid fluid) {
        return sanitize(fluid.getName() + ID_SEPARATOR + fluid.getID());
    }

    public static String imageFilePath(FluidStack fluidStack) {
        return "fluid" + File.separator
                + fluidId(fluidStack.getFluid()) + Renderer.IMAGE_FILE_EXTENSION;
    }

    /**
     * Strips out URL- and file system-unsafe characters.
     *
     * <p>Windows in particular is a bit finicky. We may need to expand this method in the future.
     * See: https://stackoverflow.com/a/48962674
     */
    public static String sanitize(String string) {
        // Note: four backslashes are needed to escape to a single backslash in the target string.
        return string.replaceAll("[<>:\"/\\\\|?*]", "");
    }

    public static String encodeBytes(byte[] input) {
        return Base64.getUrlEncoder().encodeToString(input);
    }

    public static String encodeString(String string) {
        return encodeBytes(string.getBytes(StandardCharsets.UTF_8));
    }

    public static String compressString(String string) {
        return encodeBytes(compressBytes(string.getBytes(StandardCharsets.UTF_8)));
    }

    public static String encodeNbt(NBTTagCompound nbt) {
        return compressString(nbt.toString());
    }

    public static String encodeProto(Message proto) {
        return encodeBytes(proto.toByteArray());
    }

    /** Used for recipes, which can have very long encodings. */
    public static String compressProto(Message proto) {
        return encodeBytes(compressBytes(proto.toByteArray()));
    }

    public static byte[] compressBytes(byte[] input) {
        Deflater compressor = new Deflater(Deflater.BEST_COMPRESSION);
        compressor.setInput(input);
        compressor.finish();

        byte[] outputBuffer = new byte[2 * input.length + 50];
        int outputSize = compressor.deflate(outputBuffer);
        compressor.end();

        if (!compressor.finished()) {
            // Throw because this type of failure could lead to ID collision, albeit unlikely.
            throw new RuntimeException(
                    "Compression output array too small!\nInput: " + Arrays.toString(input));
        } else {
            return Arrays.copyOfRange(outputBuffer, 0, outputSize);
        }
    }
}
