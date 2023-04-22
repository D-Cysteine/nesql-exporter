package com.github.dcysteine.nesql.exporter.plugin.quest;

import betterquesting.api.questing.IQuestLine;
import betterquesting.questing.QuestLineDatabase;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.quest.factory.QuestLineFactory;
import com.github.dcysteine.nesql.sql.quest.QuestLine;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/** Must run after {@link QuestProcessor}. */
public class QuestLineProcessor extends PluginHelper {
    public QuestLineProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        Set<Map.Entry<UUID, IQuestLine>> questLineEntries = QuestLineDatabase.INSTANCE.entrySet();
        int total = questLineEntries.size();
        logger.info("Processing {} quest lines...", total);

        QuestLineFactory questLineFactory = new QuestLineFactory(exporter);
        int count = 0;
        for (Map.Entry<UUID, IQuestLine> entry : questLineEntries) {
            count++;
            QuestLine questLine = questLineFactory.get(entry.getKey(), entry.getValue());

            if (Logger.intermittentLog(count)) {
                logger.info("Processed quest line {} of {}", count, total);
                logger.info("Most recent quest line: {}", questLine.getName());
            }
        }

        exporterState.flushEntityManager();
        logger.info("Finished processing quest lines!");
    }
}
