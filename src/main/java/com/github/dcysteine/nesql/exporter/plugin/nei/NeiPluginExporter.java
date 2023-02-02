package com.github.dcysteine.nesql.exporter.plugin.nei;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.sql.Plugin;

/** Plugin which exports the NEI item list. */
public class NeiPluginExporter extends PluginExporter {

    public NeiPluginExporter(Plugin plugin, ExporterState exporterState) {
        super(plugin, exporterState);
    }

    @Override
    public void process() {
        new NeiItemListProcessor(this).process();
    }
}
