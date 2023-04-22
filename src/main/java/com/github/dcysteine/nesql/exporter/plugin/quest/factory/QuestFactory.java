package com.github.dcysteine.nesql.exporter.plugin.quest.factory;

import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuest;
import betterquesting.api.questing.rewards.IReward;
import betterquesting.api.questing.tasks.ITask;
import betterquesting.api.utils.UuidConverter;
import betterquesting.api2.storage.DBEntry;
import betterquesting.api2.utils.QuestTranslation;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.quest.Quest;
import com.github.dcysteine.nesql.sql.quest.Reward;
import com.github.dcysteine.nesql.sql.quest.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class QuestFactory extends EntityFactory<Quest, String> {
    private final ItemFactory itemFactory;
    private final TaskFactory taskFactory;
    private final RewardFactory rewardFactory;

    public QuestFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
        taskFactory = new TaskFactory(exporter);
        rewardFactory = new RewardFactory(exporter);
    }

    public Quest get(UUID questId, IQuest quest) {
        String encodedQuestId = UuidConverter.encodeUuid(questId);
        String id = IdPrefixUtil.QUEST.applyPrefix(encodedQuestId);
        Item icon = itemFactory.get(quest.getProperty(NativeProps.ICON).getBaseStack());

        String name =
                StringUtil.stripFormatting(
                        QuestTranslation.translate(quest.getProperty(NativeProps.NAME)));
        String description =
                StringUtil.stripFormatting(
                        QuestTranslation.translate(quest.getProperty(NativeProps.DESC)));

        String visibility = quest.getProperty(NativeProps.VISIBILITY).name();
        int repeatTime = quest.getProperty(NativeProps.REPEAT_TIME);

        String questLogic = quest.getProperty(NativeProps.LOGIC_QUEST).name();
        String taskLogic = quest.getProperty(NativeProps.LOGIC_TASK).name();

        int taskIndex = 0;
        List<Task> tasks = new ArrayList<>();
        for (DBEntry<ITask> entry : quest.getTasks().getEntries()) {
            tasks.add(taskFactory.get(encodedQuestId, taskIndex++, entry.getValue()));
        }

        int rewardIndex = 0;
        List<Reward> rewards = new ArrayList<>();
        for (DBEntry<IReward> entry : quest.getRewards().getEntries()) {
            rewards.add(rewardFactory.get(encodedQuestId, rewardIndex++, entry.getValue()));
        }

        Quest questEntity =
                new Quest(
                        id, encodedQuestId, icon, name, description, visibility, repeatTime,
                        questLogic, taskLogic, tasks, rewards);
        return findOrPersist(Quest.class, questEntity);
    }

    public Quest findQuest(UUID questId) {
        String encodedQuestId = UuidConverter.encodeUuid(questId);
        String id = IdPrefixUtil.QUEST.applyPrefix(encodedQuestId);
        Quest quest = entityManager.find(Quest.class, id);
        if (quest == null) {
            throw new IllegalStateException("Could not find quest: " + questId);
        }
        return quest;
    }

    public void setRequiredQuests(Quest quest, Collection<UUID> requiredQuestIds) {
        Set<Quest> requiredQuests =
                requiredQuestIds.stream()
                        .map(this::findQuest)
                        .collect(Collectors.toCollection(HashSet::new));

        quest.setRequiredQuests(requiredQuests);
    }
}
