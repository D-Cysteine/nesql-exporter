package com.github.dcysteine.nesql.exporter.plugin.base;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.postprocessor.ItemGroupPostProcessor;
import com.github.dcysteine.nesql.exporter.plugin.base.postprocessor.RecipePostProcessor;
import com.github.dcysteine.nesql.sql.Plugin;

/** Base plugin which contains definitions for the basic tables, and logic that must be run. */
public class BasePluginExporter extends PluginExporter {

    public BasePluginExporter(Plugin plugin, ExporterState exporterState) {
        super(plugin, exporterState);
    }

    @Override
    public void postProcess() {
        new ItemGroupPostProcessor(this).postProcess();
        new RecipePostProcessor(this).postProcess();
    }
}
