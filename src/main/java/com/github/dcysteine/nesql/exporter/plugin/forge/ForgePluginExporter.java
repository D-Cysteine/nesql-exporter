package com.github.dcysteine.nesql.exporter.plugin.forge;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.forge.processor.FluidContainerProcessor;
import com.github.dcysteine.nesql.exporter.plugin.forge.processor.FluidProcessor;
import com.github.dcysteine.nesql.exporter.plugin.forge.processor.OreDictionaryProcessor;
import com.github.dcysteine.nesql.sql.Plugin;

/** Plugin which exports Forge ore dictionary and fluid registry data. */
public class ForgePluginExporter extends PluginExporter {

    public ForgePluginExporter(Plugin plugin, ExporterState exporterState) {
        super(plugin, exporterState);
    }

    @Override
    public void process() {
        new OreDictionaryProcessor(this).process();
        new FluidProcessor(this).process();
        new FluidContainerProcessor(this).process();
    }
}
