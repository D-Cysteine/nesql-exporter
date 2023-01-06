package com.github.dcysteine.nesql.exporter.util;

import com.google.protobuf.Message;
import net.minecraft.nbt.NBTTagCompound;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.zip.Deflater;

/** Utility class containing methods for handling strings and encoding things. */
public final class StringUtil {
    // Static class.
    private StringUtil() {}

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

    /** Strips out Minecraft chat formatting markers. */
    public static String stripFormatting(String string) {
        return string.replaceAll("§[0-9a-fk-or]", "");
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
        // Unfortunately, the original compressed toString() turned out to be shorter.
        //return compressProto(NbtProtoBuilder.buildNbtPb(nbt));

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
