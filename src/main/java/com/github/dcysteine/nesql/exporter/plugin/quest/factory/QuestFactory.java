package com.github.dcysteine.nesql.exporter.plugin.quest.factory;

import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuest;
import betterquesting.api.questing.rewards.IReward;
import betterquesting.api.questing.tasks.ITask;
import betterquesting.api2.storage.DBEntry;
import betterquesting.api2.utils.QuestTranslation;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.quest.Quest;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class QuestFactory extends EntityFactory<Quest, Integer> {
    private final ItemFactory itemFactory;

    public QuestFactory(EntityManager entityManager) {
        super(entityManager);
        itemFactory = new ItemFactory(entityManager);
    }

    public Quest getQuest(int id, IQuest quest) {
        Item icon = itemFactory.getItem(quest.getProperty(NativeProps.ICON).getBaseStack());

        String name =
                StringUtil.stripFormatting(
                        QuestTranslation.translate(quest.getProperty(NativeProps.NAME)));
        String description =
                StringUtil.stripFormatting(
                        QuestTranslation.translate(quest.getProperty(NativeProps.DESC)));

        // TODO need to actually examine the tasks and rewards to get something useful to show.
        // Probably need to add tables for each of these.
        List<String> tasks =
                quest.getTasks().getEntries().stream()
                        .map(DBEntry::getValue)
                        .map(ITask::getUnlocalisedName)
                        .map(QuestTranslation::translate)
                        .collect(Collectors.toCollection(ArrayList::new));
        List<String> rewards =
                quest.getRewards().getEntries().stream()
                        .map(DBEntry::getValue)
                        .map(IReward::getUnlocalisedName)
                        .map(QuestTranslation::translate)
                        .collect(Collectors.toCollection(ArrayList::new));

        Quest questEntity = new Quest(id, icon, name, description, tasks, rewards);
        return findOrPersist(Quest.class, questEntity);
    }

    public void setRequiredQuests(Quest quest, int[] requiredQuestIds) {
        Set<Quest> requiredQuests = new HashSet<>();
        for (int requiredQuestId : requiredQuestIds) {
            Optional<Quest> requiredQuestOptional = find(Quest.class, requiredQuestId);
            if (!requiredQuestOptional.isPresent()) {
                throw new IllegalStateException(
                        "Could not find required quest: " + requiredQuestId);
            }
            requiredQuests.add(requiredQuestOptional.get());
        }

        quest.setRequiredQuests(requiredQuests);
    }
}
