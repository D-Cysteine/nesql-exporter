package com.github.dcysteine.nesql.exporter.plugin.quest.factory;

import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuestLine;
import betterquesting.api2.utils.QuestTranslation;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.quest.Quest;
import com.github.dcysteine.nesql.sql.quest.QuestLine;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class QuestLineFactory extends EntityFactory<QuestLine, String> {
    private final ItemFactory itemFactory;
    private final QuestFactory questFactory;

    public QuestLineFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
        questFactory = new QuestFactory(exporter);
    }

    public QuestLine get(int questLineId, IQuestLine questLine) {
        String id = IdPrefixUtil.QUEST_LINE.applyPrefix(Integer.toString(questLineId));
        Item icon = itemFactory.get(questLine.getProperty(NativeProps.ICON).getBaseStack());

        String name =
                StringUtil.stripFormatting(
                        QuestTranslation.translate(questLine.getProperty(NativeProps.NAME)));
        String description =
                StringUtil.stripFormatting(
                        QuestTranslation.translate(questLine.getProperty(NativeProps.DESC)));
        String visibility = questLine.getProperty(NativeProps.VISIBILITY).name();

        Set<Quest> quests =
                questLine.getEntries().stream()
                        .map(entry -> questFactory.findQuest(entry.getID()))
                        .collect(Collectors.toCollection(HashSet::new));

        QuestLine questLineEntity =
                new QuestLine(id, questLineId, icon, name, description, visibility, quests);
        return findOrPersist(QuestLine.class, questLineEntity);
    }
}
