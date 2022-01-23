package com.github.dcysteine.nesql.exporter.main.config;

import com.github.dcysteine.nesql.exporter.main.Logger;
import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public final class Config {
    static final File CONFIG_FILE =
            new File(
                    (File) FMLInjectionData.data()[6],
                    "config" + File.separator + "NESQL-Exporter.cfg");
    static final Configuration CONFIG = new Configuration(CONFIG_FILE);

    // Static class.
    private Config() {}

    /** This method is only intended to be called during mod initialization. */
    public static void initialize() {
        ConfigOptions.getAllOptions().forEach(ConfigOptions.Option::initialize);
        ConfigOptions.setCategoryComments();
    }

    public static void updateConfig() {
        if (!ConfigOptions.ENABLE_CONFIG_FILE.get()) {
            // Note that simply constructing the Configuration will create an empty config file.
            // So we need to be sure to delete that (if the config file option is not enabled).
            if (CONFIG_FILE.exists()) {
                CONFIG_FILE.delete();
            }
        } else if (CONFIG.hasChanged()) {
            CONFIG.save();
            Logger.MOD.warn("Found changed config options! Config file has been updated.");
        }
    }

    static String getConfigFilePath() {
        return CONFIG_FILE.getAbsolutePath();
    }
}
