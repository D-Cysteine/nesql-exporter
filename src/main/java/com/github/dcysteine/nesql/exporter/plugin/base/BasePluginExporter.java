package com.github.dcysteine.nesql.exporter.plugin.base;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.postprocessor.ItemGroupPostProcessor;
import com.github.dcysteine.nesql.exporter.plugin.base.postprocessor.RecipePostProcessor;
import com.github.dcysteine.nesql.exporter.plugin.base.processor.CraftingRecipeProcessor;
import com.github.dcysteine.nesql.exporter.plugin.base.processor.FurnaceRecipeProcessor;
import com.github.dcysteine.nesql.sql.Plugin;

/** Base plugin which handles vanilla Minecraft as well as Forge recipes. */
public class BasePluginExporter extends PluginExporter {
    private final BaseRecipeTypeHandler recipeTypeHandler;

    public BasePluginExporter(Plugin plugin, ExporterState exporterState) {
        super(plugin, exporterState);
        recipeTypeHandler = new BaseRecipeTypeHandler(this);
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

    @Override
    public void postProcess() {
        new ItemGroupPostProcessor(this).postProcess();
        new RecipePostProcessor(this).postProcess();
    }
}
