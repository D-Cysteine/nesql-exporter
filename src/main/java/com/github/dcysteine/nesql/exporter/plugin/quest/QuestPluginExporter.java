package com.github.dcysteine.nesql.exporter.plugin.quest;

import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.quest.postprocessor.QuestPostProcessor;
import com.github.dcysteine.nesql.exporter.plugin.quest.processor.QuestProcessor;
import jakarta.persistence.EntityManager;

/** Plugin which exports BetterQuesting quests. */
public class QuestPluginExporter implements PluginExporter {
    private final EntityManager entityManager;

    public QuestPluginExporter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void process() {
        new QuestProcessor(entityManager).process();
    }

    @Override
    public void postProcess() {
        new QuestPostProcessor(entityManager).postProcess();
    }
}
