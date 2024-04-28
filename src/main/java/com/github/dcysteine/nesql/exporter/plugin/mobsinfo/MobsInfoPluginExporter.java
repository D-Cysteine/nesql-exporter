package com.github.dcysteine.nesql.exporter.plugin.mobsinfo;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.sql.Plugin;

/** Plugin which handles vanilla Minecraft recipes. */
public class MobsInfoPluginExporter extends PluginExporter {

    public MobsInfoPluginExporter(Plugin plugin, ExporterState exporterState) {
        super(plugin, exporterState);
    }

    @Override
    public void process() {
        new MobsInfoProcessor(this).process();
    }
}
