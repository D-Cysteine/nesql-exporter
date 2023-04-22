package com.github.dcysteine.nesql.exporter.plugin.quest.factory;

import betterquesting.api.questing.rewards.IReward;
import betterquesting.api.utils.UuidConverter;
import betterquesting.api2.utils.QuestTranslation;
import bq_standard.rewards.RewardChoice;
import bq_standard.rewards.RewardCommand;
import bq_standard.rewards.RewardItem;
import bq_standard.rewards.RewardQuestCompletion;
import bq_standard.rewards.RewardXP;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemGroupFactory;
import com.github.dcysteine.nesql.exporter.plugin.quest.QuestUtil;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.quest.Reward;
import com.github.dcysteine.nesql.sql.quest.RewardType;

import java.util.ArrayList;
import java.util.List;

public class RewardFactory extends EntityFactory<Reward, String> {
    private final ItemGroupFactory itemGroupFactory;

    public RewardFactory(PluginExporter exporter) {
        super(exporter);
        itemGroupFactory = new ItemGroupFactory(exporter);
    }

    public Reward get(String encodedQuestId, int index, IReward reward) {
        String id =
                IdPrefixUtil.QUEST_REWARD.applyPrefix(encodedQuestId, Integer.toString(index));
        String name = QuestTranslation.translate(reward.getUnlocalisedName());

        Reward rewardEntity;
        if (reward instanceof RewardItem) {
            RewardItem typedReward = (RewardItem) reward;
            List<ItemGroup> items = QuestUtil.buildItems(itemGroupFactory, typedReward.items);
            rewardEntity = new Reward(id, name, RewardType.ITEM, items, "", 0, false, "");

        } else if (reward instanceof RewardChoice) {
            RewardChoice typedReward = (RewardChoice) reward;
            List<ItemGroup> items = QuestUtil.buildItems(itemGroupFactory, typedReward.choices);
            rewardEntity = new Reward(id, name, RewardType.CHOICE, items, "", 0, false, "");

        } else if (reward instanceof RewardCommand) {
            RewardCommand typedReward = (RewardCommand) reward;
            rewardEntity =
                    new Reward(
                            id, name, RewardType.COMMAND, new ArrayList<>(),
                            typedReward.command, 0, false, "");

        } else if (reward instanceof RewardXP) {
            RewardXP typedReward = (RewardXP) reward;
            rewardEntity =
                    new Reward(
                            id, name, RewardType.XP, new ArrayList<>(),
                            "", typedReward.amount, typedReward.levels, "");

        } else if (reward instanceof RewardQuestCompletion) {
            RewardQuestCompletion typedReward = (RewardQuestCompletion) reward;
            rewardEntity =
                    new Reward(
                            id, name, RewardType.COMPLETE_QUEST, new ArrayList<>(),
                            "", 0, false, UuidConverter.encodeUuid(typedReward.questNum));

        } else {
            // TODO add any additional reward types that we need to handle here.
            logger.warn("Unhandled reward type: " + reward);
            rewardEntity =
                    new Reward(id, name, RewardType.UNHANDLED, new ArrayList<>(), "", 0, false, "");
        }

        return findOrPersist(Reward.class, rewardEntity);
    }
}
