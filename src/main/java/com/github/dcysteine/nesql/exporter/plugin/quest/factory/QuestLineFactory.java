package com.github.dcysteine.nesql.exporter.plugin.quest.factory;

import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuestLine;
import betterquesting.api.utils.UuidConverter;
import betterquesting.api2.utils.QuestTranslation;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.quest.QuestLine;

import java.util.UUID;

public class QuestLineFactory extends EntityFactory<QuestLine, String> {
    private final ItemFactory itemFactory;
    private final QuestLineEntryFactory questLineEntryFactory;

    public QuestLineFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
        questLineEntryFactory = new QuestLineEntryFactory(exporter);
    }

    public QuestLine get(UUID questLineId, IQuestLine questLine) {
        String encodedQuestLineId = UuidConverter.encodeUuid(questLineId);
        String id = IdPrefixUtil.QUEST_LINE.applyPrefix(encodedQuestLineId);
        Item icon = itemFactory.get(questLine.getProperty(NativeProps.ICON).getBaseStack());

        String name =
                StringUtil.stripFormatting(
                        QuestTranslation.translate(questLine.getProperty(NativeProps.NAME)));
        String description =
                StringUtil.stripFormatting(
                        QuestTranslation.translate(questLine.getProperty(NativeProps.DESC)));
        String visibility = questLine.getProperty(NativeProps.VISIBILITY).name();

        QuestLine questLineEntity =
                new QuestLine(id, encodedQuestLineId, icon, name, description, visibility);

        // No need to actually do anything with the returned QuestLineEntry objects, because the
        // QuestLineEntry has ownership of the bidirectional QuestLine <-> QuestLineEntry link.
        questLine.forEach((k, v) -> questLineEntryFactory.get(questLineEntity, questLineId, k, v));

        return findOrPersist(QuestLine.class, questLineEntity);
    }
}
