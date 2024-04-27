package com.github.dcysteine.nesql.exporter.plugin.quest.factory;

import betterquesting.api.questing.IQuestLineEntry;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.sql.quest.Quest;
import com.github.dcysteine.nesql.sql.quest.QuestLine;
import com.github.dcysteine.nesql.sql.quest.QuestLineEntry;

import java.util.UUID;

public class QuestLineEntryFactory extends EntityFactory<QuestLineEntry, String> {
    private final QuestFactory questFactory;

    public QuestLineEntryFactory(PluginExporter exporter) {
        super(exporter);
        questFactory = new QuestFactory(exporter);
    }

    public QuestLineEntry get(
            QuestLine questLine, UUID questLineId, UUID questId, IQuestLineEntry questLineEntry) {
        String id =
                IdPrefixUtil.QUEST_LINE_ENTRY.applyPrefix(
                        IdUtil.questLineEntryId(questLineId, questId));
        int posX = questLineEntry.getPosX();
        int posY = questLineEntry.getPosY();
        int sizeX = questLineEntry.getSizeX();
        int sizeY = questLineEntry.getSizeY();
        Quest quest = questFactory.findQuest(questId);

        QuestLineEntry questLineEntryEntity =
                new QuestLineEntry(id, posX, posY, sizeX, sizeY, questLine, quest);
        return findOrPersist(QuestLineEntry.class, questLineEntryEntity);
    }
}
