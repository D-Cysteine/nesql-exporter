package com.github.dcysteine.nesql.exporter.plugin.quest;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.sql.Plugin;

/** Plugin which exports BetterQuesting quests. */
public class QuestPluginExporter extends PluginExporter {
    public QuestPluginExporter(Plugin plugin, ExporterState exporterState) {
        super(plugin, exporterState);
    }

    @Override
    public void process() {
        new QuestProcessor(this).process();
        new QuestLineProcessor(this).process();
    }

    @Override
    public void postProcess() {
        new QuestPostProcessor(this).postProcess();
    }
}
