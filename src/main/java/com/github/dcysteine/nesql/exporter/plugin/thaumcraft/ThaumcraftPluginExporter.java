package com.github.dcysteine.nesql.exporter.plugin.thaumcraft;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.sql.Plugin;

/** Plugin which exports BetterQuesting quests. */
public class ThaumcraftPluginExporter extends PluginExporter {
    public ThaumcraftPluginExporter(Plugin plugin, ExporterState exporterState) {
        super(plugin, exporterState);
    }

    @Override
    public void initialize() {
        exporterState.addItemListener(new AspectEntryListener(this));
    }

    @Override
    public void process() {
        new AspectProcessor(this).process();
    }

    @Override
    public void postProcess() {
        new AspectPostProcessor(this).postProcess();
    }
}
