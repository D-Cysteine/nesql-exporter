package com.github.dcysteine.nesql.exporter.main;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.LogManager;

public final class Logger {
    public static final org.apache.logging.log4j.Logger MOD =
            LogManager.getLogger(NotEnoughSQLExporter.MOD_NAME);

    // Static class.
    private Logger() {}

    public static void chatMessage(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }
}
