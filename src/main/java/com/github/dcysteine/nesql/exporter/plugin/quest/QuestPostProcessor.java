package com.github.dcysteine.nesql.exporter.plugin.quest;

import betterquesting.api.questing.IQuest;
import betterquesting.questing.QuestDatabase;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.quest.factory.QuestFactory;
import com.github.dcysteine.nesql.sql.quest.Quest;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class QuestPostProcessor extends PluginHelper {
    public QuestPostProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void postProcess() {
        Set<Map.Entry<UUID, IQuest>> questEntries = QuestDatabase.INSTANCE.entrySet();
        int total = questEntries.size();
        logger.info("Post-processing {} quests...", total);

        QuestFactory questFactory = new QuestFactory(exporter);
        int count = 0;
        for (Map.Entry<UUID, IQuest> entry : questEntries) {
            count++;

            Quest quest = questFactory.findQuest(entry.getKey());
            questFactory.setRequiredQuests(quest, entry.getValue().getRequirements());

            if (Logger.intermittentLog(count)) {
                logger.info("Post-processed quest {} of {}", count, total);
                logger.info("Most recent quest: {}", quest.getName());
            }
        }

        exporterState.flushEntityManager();
        logger.info("Finished post-processing quests!");
    }
}
