package com.github.dcysteine.nesql.exporter.plugin.quest.processor;

import betterquesting.api.questing.IQuest;
import betterquesting.api2.storage.DBEntry;
import betterquesting.questing.QuestDatabase;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.quest.factory.QuestFactory;
import com.github.dcysteine.nesql.sql.quest.Quest;
import jakarta.persistence.EntityManager;

import java.util.List;

public class QuestProcessor {
    private final EntityManager entityManager;

    public QuestProcessor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void process() {
        List<DBEntry<IQuest>> questEntries = QuestDatabase.INSTANCE.getEntries();
        int total = questEntries.size();
        Logger.BASE.info("Processing {} quests...", total);

        QuestFactory questFactory = new QuestFactory(entityManager);
        int count = 0;
        for (DBEntry<IQuest> entry : questEntries) {
            count++;
            Quest quest = questFactory.getQuest(entry.getID(), entry.getValue());

            if (Logger.intermittentLog(count)) {
                Logger.BASE.info("Processed quest {} of {}", count, total);
                Logger.BASE.info("Most recent quest: {}", quest.getName());
            }
        }

        Logger.BASE.info("Finished processing quests!");
    }
}
