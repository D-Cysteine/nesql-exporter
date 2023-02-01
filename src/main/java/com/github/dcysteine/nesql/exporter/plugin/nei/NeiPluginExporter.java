package com.github.dcysteine.nesql.exporter.plugin.nei;

import com.github.dcysteine.nesql.exporter.plugin.Database;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.nei.processor.NeiItemListProcessor;

/** Plugin which exports the NEI item list. */
public class NeiPluginExporter implements PluginExporter {
    private final Database database;

    public NeiPluginExporter(Database database) {
        this.database = database;
    }

    @Override
    public void process() {
        new NeiItemListProcessor(database).process();
    }
}
