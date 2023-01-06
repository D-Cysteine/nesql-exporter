package com.github.dcysteine.nesql.exporter.util;

import com.google.protobuf.Message;
import net.minecraft.nbt.NBTTagCompound;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/** Utility class containing methods for handling strings and encoding things. */
public final class StringUtil {
    // Static class.
    private StringUtil() {}

    /** Strips out Minecraft chat formatting markers. */
    public static String stripFormatting(String string) {
        return string.replaceAll("§[0-9a-fk-or]", "");
    }

    public static String encodeBytes(byte[] input) {
        // This does have a chance of collision, but it should be miniscule (2^64 entries for 50%),
        // according to
        // https://stackoverflow.com/questions/201705/how-many-random-elements-before-md5-produces-collisions
        // Replace with Base64.getUrlEncoder().encodeToString(input); if we have to.
        return UUID.nameUUIDFromBytes(input).toString();
    }

    public static String encodeString(String string) {
        return encodeBytes(string.getBytes(StandardCharsets.UTF_8));
    }

    public static String encodeNbt(NBTTagCompound nbt) {
        return encodeString(nbt.toString());
    }

    public static String encodeProto(Message proto) {
        return encodeBytes(proto.toByteArray());
    }
}
