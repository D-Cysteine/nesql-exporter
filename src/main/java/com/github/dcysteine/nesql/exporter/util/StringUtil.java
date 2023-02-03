package com.github.dcysteine.nesql.exporter.util;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Longs;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Message;
import net.minecraft.nbt.NBTTagCompound;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/** Utility class containing methods for handling strings and encoding things. */
public final class StringUtil {
    // Static class.
    private StringUtil() {}

    /**
     * Replaces system-specific file separators with forward slash.
     *
     * <p>Call this method before saving file paths to the database, since the images are being
     * served by a web server and should therefore use forward slash.
     */
    public static String formatFilePath(String filePath) {
        return filePath.replace(File.separatorChar, '/');
    }

    /** Strips out Minecraft chat formatting markers. */
    public static String stripFormatting(String string) {
        return string.replaceAll("§[0-9a-fk-or]", "");
    }

    public static String encodeBytes(byte[] input) {
        // This does have a chance of collision, but it should be minuscule (2^64 entries for 50%),
        // according to
        // https://stackoverflow.com/questions/201705/how-many-random-elements-before-md5-produces-collisions
        UUID uuid = UUID.nameUUIDFromBytes(input);

        byte[] upper = Longs.toByteArray(uuid.getMostSignificantBits());
        byte[] lower = Longs.toByteArray(uuid.getLeastSignificantBits());

        return Base64.getUrlEncoder().encodeToString(Bytes.concat(upper, lower));
    }

    public static String encodeString(String string) {
        return encodeBytes(string.getBytes(StandardCharsets.UTF_8));
    }

    public static String encodeNbt(NBTTagCompound nbt) {
        return encodeString(nbt.toString());
    }

    public static String encodeProto(Message proto) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        CodedOutputStream outputStream = CodedOutputStream.newInstance(byteStream);
        // Necessary to ensure that maps are serialized in deterministic order.
        // See: https://gist.github.com/kchristidis/39c8b310fd9da43d515c4394c3cd9510
        outputStream.useDeterministicSerialization();

        try {
            outputStream.writeMessageNoTag(proto);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return encodeBytes(byteStream.toByteArray());
    }
}
