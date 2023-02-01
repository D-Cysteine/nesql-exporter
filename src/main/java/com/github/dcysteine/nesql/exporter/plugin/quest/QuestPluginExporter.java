package com.github.dcysteine.nesql.exporter.plugin.quest;

import com.github.dcysteine.nesql.exporter.plugin.Database;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.quest.postprocessor.QuestPostProcessor;
import com.github.dcysteine.nesql.exporter.plugin.quest.processor.QuestProcessor;

/** Plugin which exports BetterQuesting quests. */
public class QuestPluginExporter implements PluginExporter {
    private final Database database;

    public QuestPluginExporter(Database database) {
        this.database = database;
    }

    @Override
    public void process() {
        new QuestProcessor(database).process();
    }

    @Override
    public void postProcess() {
        new QuestPostProcessor(database).postProcess();
    }
}
