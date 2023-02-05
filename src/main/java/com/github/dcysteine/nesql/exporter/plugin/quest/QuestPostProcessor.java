package com.github.dcysteine.nesql.exporter.plugin.quest;

import betterquesting.api.questing.IQuest;
import betterquesting.api2.storage.DBEntry;
import betterquesting.questing.QuestDatabase;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.quest.factory.QuestFactory;
import com.github.dcysteine.nesql.sql.quest.Quest;

import java.util.List;

public class QuestPostProcessor extends PluginHelper {
    public QuestPostProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void postProcess() {
        List<DBEntry<IQuest>> questEntries = QuestDatabase.INSTANCE.getEntries();
        int total = questEntries.size();
        logger.info("Post-processing {} quests...", total);

        QuestFactory questFactory = new QuestFactory(exporter);
        int count = 0;
        for (DBEntry<IQuest> entry : questEntries) {
            count++;

            Quest quest = questFactory.findQuest(entry.getID());
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
