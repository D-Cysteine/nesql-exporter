package com.github.dcysteine.nesql.exporter.plugin.quest.factory;

import betterquesting.api.questing.tasks.ITask;
import betterquesting.api2.utils.QuestTranslation;
import bq_standard.tasks.TaskCheckbox;
import bq_standard.tasks.TaskCrafting;
import bq_standard.tasks.TaskFluid;
import bq_standard.tasks.TaskHunt;
import bq_standard.tasks.TaskLocation;
import bq_standard.tasks.TaskRetrieval;
import com.github.dcysteine.nesql.exporter.common.MobSpec;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.FluidFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemGroupFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.MobFactory;
import com.github.dcysteine.nesql.exporter.plugin.quest.QuestUtil;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.mob.Mob;
import com.github.dcysteine.nesql.sql.quest.Task;
import com.github.dcysteine.nesql.sql.quest.TaskType;

import java.util.ArrayList;
import java.util.List;

public class TaskFactory extends EntityFactory<Task, String> {
    private final ItemGroupFactory itemGroupFactory;
    private final FluidFactory fluidFactory;
    private final MobFactory mobFactory;

    public TaskFactory(PluginExporter exporter) {
        super(exporter);
        itemGroupFactory = new ItemGroupFactory(exporter);
        fluidFactory = new FluidFactory(exporter);
        mobFactory = new MobFactory(exporter);
    }

    public Task get(String encodedQuestId, int index, ITask task) {
        String id =
                IdPrefixUtil.QUEST_TASK.applyPrefix(encodedQuestId, Integer.toString(index));
        String name = QuestTranslation.translate(task.getUnlocalisedName());

        Task taskEntity;
        if (task instanceof TaskRetrieval) {
            TaskRetrieval typedTask = (TaskRetrieval) task;
            List<ItemGroup> items = QuestUtil.buildItems(itemGroupFactory, typedTask.requiredItems);
            taskEntity =
                    new Task(
                            id, name, TaskType.RETRIEVAL, items, new ArrayList<>(),
                            typedTask.consume, null, 0, "");

        } else if (task instanceof TaskCrafting) {
            TaskCrafting typedTask = (TaskCrafting) task;
            List<ItemGroup> items = QuestUtil.buildItems(itemGroupFactory, typedTask.requiredItems);
            taskEntity =
                    new Task(
                            id, name, TaskType.CRAFTING, items, new ArrayList<>(),
                            false, null, 0, "");

        } else if (task instanceof TaskFluid) {
            TaskFluid typedTask = (TaskFluid) task;
            List<FluidStack> fluids = QuestUtil.buildFluids(fluidFactory, typedTask.requiredFluids);
            taskEntity =
                    new Task(
                            id, name, TaskType.FLUID, new ArrayList<>(), fluids, false, null, 0, "");

        } else if (task instanceof TaskCheckbox) {
            taskEntity =
                    new Task(
                            id, name, TaskType.CHECKBOX, new ArrayList<>(), new ArrayList<>(),
                            false, null, 0, "");

        } else if (task instanceof TaskHunt) {
            TaskHunt typedTask = (TaskHunt) task;

            Mob mob;
            if (!typedTask.ignoreNBT) {
                mob = mobFactory.get(MobSpec.create(typedTask.idName, typedTask.targetTags));
            } else {
                mob = mobFactory.get(MobSpec.create(typedTask.idName));
            }

            taskEntity =
                    new Task(
                            id, name, TaskType.HUNT, new ArrayList<>(), new ArrayList<>(),
                            false, mob, typedTask.required, "");


        } else if (task instanceof TaskLocation) {
            // TODO do we need to handle the other fields in TaskLocation, like biome or structure?
            TaskLocation typedTask = (TaskLocation) task;
            taskEntity =
                    new Task(
                            id, name, TaskType.LOCATION, new ArrayList<>(), new ArrayList<>(),
                            false, null, 0, TaskLocation.getDimName(typedTask.dim));

        } else {
            // TODO add any additional task types that we need to handle here.
            logger.warn("Unhandled task type: " + task);
            taskEntity =
                    new Task(
                            id, name, TaskType.UNHANDLED, new ArrayList<>(), new ArrayList<>(),
                            false, null, 0, "");
        }

        return findOrPersist(Task.class, taskEntity);
    }
}
