package com.github.dcysteine.nesql.exporter.plugin.quest.factory;

import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuest;
import betterquesting.api.questing.rewards.IReward;
import betterquesting.api.questing.tasks.ITask;
import betterquesting.api.utils.BigItemStack;
import betterquesting.api2.storage.DBEntry;
import betterquesting.api2.utils.QuestTranslation;
import bq_standard.rewards.RewardChoice;
import bq_standard.rewards.RewardCommand;
import bq_standard.rewards.RewardItem;
import bq_standard.rewards.RewardXP;
import bq_standard.tasks.TaskCheckbox;
import bq_standard.tasks.TaskCrafting;
import bq_standard.tasks.TaskHunt;
import bq_standard.tasks.TaskRetrieval;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.Database;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.quest.Quest;
import com.github.dcysteine.nesql.sql.quest.Reward;
import com.github.dcysteine.nesql.sql.quest.RewardType;
import com.github.dcysteine.nesql.sql.quest.Task;
import com.github.dcysteine.nesql.sql.quest.TaskType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class QuestFactory extends EntityFactory<Quest, String> {
    private final ItemFactory itemFactory;

    public QuestFactory(Database database) {
        super(database);
        itemFactory = new ItemFactory(database);
    }

    public Quest getQuest(int questId, IQuest quest) {
        String id = IdPrefixUtil.QUEST.applyPrefix(Integer.toString(questId));
        Item icon = itemFactory.getItem(quest.getProperty(NativeProps.ICON).getBaseStack());

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

        List<Task> tasks =
                quest.getTasks().getEntries().stream()
                        .map(DBEntry::getValue)
                        .map(this::buildTask)
                        .collect(Collectors.toCollection(ArrayList::new));
        List<Reward> rewards =
                quest.getRewards().getEntries().stream()
                        .map(DBEntry::getValue)
                        .map(this::buildReward)
                        .collect(Collectors.toCollection(ArrayList::new));

        Quest questEntity =
                new Quest(
                        id, questId, icon, name, description, visibility, repeatTime,
                        questLogic, taskLogic, tasks, rewards);
        return findOrPersist(Quest.class, questEntity);
    }

    public Quest findQuest(int questId) {
        String id = IdPrefixUtil.QUEST.applyPrefix(Integer.toString(questId));
        Quest quest = entityManager.find(Quest.class, id);
        if (quest == null) {
            throw new IllegalStateException("Could not find quest: " + questId);
        }
        return quest;
    }

    public void setRequiredQuests(Quest quest, int[] requiredQuestIds) {
        Set<Quest> requiredQuests =
                Arrays.stream(requiredQuestIds)
                        .mapToObj(this::findQuest)
                        .collect(Collectors.toCollection(HashSet::new));

        quest.setRequiredQuests(requiredQuests);
    }

    private List<Item> buildItemList(List<BigItemStack> items) {
        return items.stream()
                .map(BigItemStack::getBaseStack)
                .map(itemFactory::getItem)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Task buildTask(ITask task) {
        String name = QuestTranslation.translate(task.getUnlocalisedName());

        if (task instanceof TaskRetrieval) {
            TaskRetrieval typedTask = (TaskRetrieval) task;
            return new Task(
                    name, TaskType.RETRIEVAL, buildItemList(typedTask.requiredItems), "", 0);
        }

        if (task instanceof TaskCrafting) {
            TaskCrafting typedTask = (TaskCrafting) task;
            return new Task(
                    name, TaskType.CRAFTING, buildItemList(typedTask.requiredItems), "", 0);
        }

        if (task instanceof TaskCheckbox) {
            return new Task(name, TaskType.CHECKBOX, new ArrayList<>(), "", 0);
        }

        if (task instanceof TaskHunt) {
            TaskHunt typedTask = (TaskHunt) task;
            return new Task(
                    name, TaskType.HUNT, new ArrayList<>(), typedTask.idName, typedTask.required);
        }

        // TODO add any additional task types that we need to handle here.
        Logger.QUEST.warn("Unhandled task type: " + task);
        return new Task(name, TaskType.UNHANDLED, new ArrayList<>(), "", 0);
    }

    private Reward buildReward(IReward reward) {
        String name = QuestTranslation.translate(reward.getUnlocalisedName());

        if (reward instanceof RewardItem) {
            RewardItem typedReward = (RewardItem) reward;
            return new Reward(
                    name, RewardType.ITEM, buildItemList(typedReward.items), "", 0, false);
        }

        if (reward instanceof RewardChoice) {
            RewardChoice typedReward = (RewardChoice) reward;
            return new Reward(
                    name, RewardType.CHOICE, buildItemList(typedReward.choices), "", 0, false);
        }

        if (reward instanceof RewardCommand) {
            RewardCommand typedReward = (RewardCommand) reward;
            return new Reward(
                    name, RewardType.COMMAND, new ArrayList<>(), typedReward.command, 0, false);
        }

        if (reward instanceof RewardXP) {
            RewardXP typedReward = (RewardXP) reward;
            return new Reward(
                    name, RewardType.XP, new ArrayList<>(),
                    "", typedReward.amount, typedReward.levels);
        }

        // TODO add any additional reward types that we need to handle here.
        Logger.QUEST.warn("Unhandled reward type: " + reward);
        return new Reward(name, RewardType.UNHANDLED, new ArrayList<>(), "", 0, false);
    }
}
