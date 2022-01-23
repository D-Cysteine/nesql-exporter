package com.github.dcysteine.nesql.exporter.main.config;

import com.github.dcysteine.nesql.exporter.main.NotEnoughSQLExporter;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

import java.util.List;
import java.util.stream.Collectors;

public final class ConfigGui extends GuiConfig {
    public ConfigGui(GuiScreen parent) {
        super(
                parent,
                getConfigOptions(),
                NotEnoughSQLExporter.MOD_ID,
                false,
                false,
                GuiConfig.getAbridgedConfigPath(Config.getConfigFilePath()),
                NotEnoughSQLExporter.MOD_NAME);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Config.updateConfig();
    }

    @SuppressWarnings("rawtypes")
    private static List<IConfigElement> getConfigOptions() {
        return ConfigOptions.getAllOptions().stream()
                .map(option -> new ConfigElement(option.getProperty()))
                .collect(Collectors.toList());
    }
}
