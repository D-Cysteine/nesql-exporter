package com.github.dcysteine.nesql.exporter.plugin.gregtech;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.sql.Plugin;

/** Plugin which exports GT5 recipes. */
public class GregTechPluginExporter extends PluginExporter {
    public GregTechPluginExporter(Plugin plugin, ExporterState exporterState) {
        super(plugin, exporterState);
    }

    @Override
    public void process() {
        new GregTechRecipeProcessor(this).process();
    }
}
