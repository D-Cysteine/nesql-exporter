package com.github.dcysteine.nesql.exporter.plugin.thaumcraft;

import com.github.dcysteine.nesql.exporter.plugin.Database;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.thaumcraft.listener.AspectEntryListener;
import com.github.dcysteine.nesql.exporter.plugin.thaumcraft.postprocessor.AspectPostProcessor;
import com.github.dcysteine.nesql.exporter.plugin.thaumcraft.processor.AspectProcessor;

/** Plugin which exports BetterQuesting quests. */
public class ThaumcraftPluginExporter implements PluginExporter {
    private final Database database;

    public ThaumcraftPluginExporter(Database database) {
        this.database = database;
    }

    @Override
    public void registerListeners() {
        database.addItemListener(new AspectEntryListener(database));
    }

    @Override
    public void process() {
        new AspectProcessor(database).process();
    }

    @Override
    public void postProcess() {
        new AspectPostProcessor(database).postProcess();
    }
}
