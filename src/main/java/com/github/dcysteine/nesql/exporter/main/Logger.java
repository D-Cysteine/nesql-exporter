package com.github.dcysteine.nesql.exporter.main;

import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.LogManager;

public final class Logger {
    public static final org.apache.logging.log4j.Logger MOD =
            LogManager.getLogger(Main.MOD_NAME);
    public static final org.apache.logging.log4j.Logger BASE =
            LogManager.getLogger(Main.MOD_NAME + "/base");

    // Static class.
    private Logger() {}

    public static void chatMessage(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }

    /** Returns whether we should log a message, given the configured logging interval. */
    public static boolean intermittentLog(int count) {
        int loggingFrequency = ConfigOptions.LOGGING_FREQUENCY.get();
        if (loggingFrequency <= 0) {
            return false;
        }

        return count % loggingFrequency == 0;
    }

    /**
     * Logs a message at the configured logging interval.
     *
     * @param formatString a format string containing exactly one {@code "{}"}, which will be
     *                     replaced with {@code count}
     * @param count a count of the current progress (# of things processed)
     * @return whether the message was logged
     */
    public static boolean intermittentLog(String formatString, int count) {
        boolean shouldLog = intermittentLog(count);
        if (shouldLog) {
            MOD.info(formatString, count);
        }
        return shouldLog;
    }
}
