package com.github.dcysteine.nesql.exporter.plugin.quest.processor;

import betterquesting.api.questing.IQuest;
import betterquesting.api2.storage.DBEntry;
import betterquesting.questing.QuestDatabase;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.Database;
import com.github.dcysteine.nesql.exporter.plugin.quest.factory.QuestFactory;
import com.github.dcysteine.nesql.sql.quest.Quest;

import java.util.List;

public class QuestProcessor {
    private final Database database;

    public QuestProcessor(Database database) {
        this.database = database;
    }

    public void process() {
        List<DBEntry<IQuest>> questEntries = QuestDatabase.INSTANCE.getEntries();
        int total = questEntries.size();
        Logger.QUEST.info("Processing {} quests...", total);

        QuestFactory questFactory = new QuestFactory(database);
        int count = 0;
        for (DBEntry<IQuest> entry : questEntries) {
            count++;
            Quest quest = questFactory.getQuest(entry.getID(), entry.getValue());

            if (Logger.intermittentLog(count)) {
                Logger.QUEST.info("Processed quest {} of {}", count, total);
                Logger.QUEST.info("Most recent quest: {}", quest.getName());
            }
        }

        Logger.QUEST.info("Finished processing quests!");
    }
}
