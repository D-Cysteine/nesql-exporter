package com.github.dcysteine.nesql.exporter.plugin.minecraft;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.sql.Plugin;

/** Plugin which handles vanilla Minecraft recipes. */
public class MinecraftPluginExporter extends PluginExporter {
    private final MinecraftRecipeTypeHandler recipeTypeHandler;

    public MinecraftPluginExporter(Plugin plugin, ExporterState exporterState) {
        super(plugin, exporterState);
        recipeTypeHandler = new MinecraftRecipeTypeHandler(this);
    }

    @Override
    public void initialize() {
        recipeTypeHandler.initialize();
    }

    @Override
    public void process() {
        new CraftingRecipeProcessor(this, recipeTypeHandler).process();
        new FurnaceRecipeProcessor(this, recipeTypeHandler).process();
    }
}
