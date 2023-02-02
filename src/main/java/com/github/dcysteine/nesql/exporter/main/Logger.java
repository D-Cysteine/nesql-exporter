package com.github.dcysteine.nesql.exporter.main;

import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.sql.Plugin;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.LogManager;

public final class Logger {
    public static final org.apache.logging.log4j.Logger MOD = LogManager.getLogger(Main.MOD_NAME);

    // Static class.
    private Logger() {}

    public static org.apache.logging.log4j.Logger getLogger(Plugin plugin) {
        return LogManager.getLogger(String.format("%s/%s", Main.MOD_NAME, plugin.getName()));
    }

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
     * @param logger the logger to use
     * @param formatString a format string containing exactly one {@code "{}"}, which will be
     *                     replaced with {@code count}
     * @param count a count of the current progress (# of things processed)
     * @return whether the message was logged
     */
    public static boolean intermittentLog(
            org.apache.logging.log4j.Logger logger, String formatString, int count) {
        if (intermittentLog(count)) {
            logger.info(formatString, count);
            return true;
        }
        return false;
    }
}
