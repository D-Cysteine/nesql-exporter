package com.github.dcysteine.nesql.exporter.plugin.quest.postprocessor;

import betterquesting.api.questing.IQuest;
import betterquesting.api2.storage.DBEntry;
import betterquesting.questing.QuestDatabase;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.quest.factory.QuestFactory;
import com.github.dcysteine.nesql.sql.quest.Quest;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class QuestPostProcessor {
    private final EntityManager entityManager;

    public QuestPostProcessor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void postProcess() {
        List<DBEntry<IQuest>> questEntries = QuestDatabase.INSTANCE.getEntries();
        int total = questEntries.size();
        Logger.BASE.info("Post-processing {} quests...", total);

        QuestFactory questFactory = new QuestFactory(entityManager);
        int count = 0;
        for (DBEntry<IQuest> entry : questEntries) {
            count++;

            int id = entry.getID();
            Optional<Quest> questOptional = questFactory.find(Quest.class, id);
            if (!questOptional.isPresent()) {
                throw new IllegalStateException("Could not find quest: " + id);
            }
            Quest quest = questOptional.get();
            questFactory.setRequiredQuests(quest, entry.getValue().getRequirements());

            if (Logger.intermittentLog(count)) {
                Logger.BASE.info("Post-processed quest {} of {}", count, total);
                Logger.BASE.info("Most recent quest: {}", quest.getName());
            }
        }

        Logger.BASE.info("Finished post-processing quests!");
    }
}
