package com.github.dcysteine.nesql.exporter.plugin.quest.factory;

import betterquesting.api.questing.tasks.ITask;
import betterquesting.api2.utils.QuestTranslation;
import bq_standard.tasks.TaskCheckbox;
import bq_standard.tasks.TaskCrafting;
import bq_standard.tasks.TaskFluid;
import bq_standard.tasks.TaskHunt;
import bq_standard.tasks.TaskLocation;
import bq_standard.tasks.TaskRetrieval;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.FluidFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.plugin.quest.QuestUtil;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.github.dcysteine.nesql.sql.quest.Task;
import com.github.dcysteine.nesql.sql.quest.TaskType;

import java.util.ArrayList;
import java.util.List;

public class TaskFactory extends EntityFactory<Task, String> {
    private final ItemFactory itemFactory;
    private final FluidFactory fluidFactory;

    public TaskFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
        fluidFactory = new FluidFactory(exporter);
    }

    public Task getTask(int questId, int index, ITask task) {
        String id = IdPrefixUtil.QUEST_TASK.applyPrefix(questId + IdUtil.ID_SEPARATOR + index);
        String name = QuestTranslation.translate(task.getUnlocalisedName());

        Task taskEntity;
        if (task instanceof TaskRetrieval) {
            TaskRetrieval typedTask = (TaskRetrieval) task;
            List<ItemStack> itemStacks =
                    QuestUtil.buildItemStacks(itemFactory, typedTask.requiredItems);
            taskEntity =
                    new Task(
                            id, name, TaskType.RETRIEVAL, itemStacks, new ArrayList<>(), "", 0, "");

        } else if (task instanceof TaskCrafting) {
            TaskCrafting typedTask = (TaskCrafting) task;
            List<ItemStack> itemStacks =
                    QuestUtil.buildItemStacks(itemFactory, typedTask.requiredItems);
            taskEntity =
                    new Task(id, name, TaskType.CRAFTING, itemStacks, new ArrayList<>(), "", 0, "");

        } else if (task instanceof TaskFluid) {
            TaskFluid typedTask = (TaskFluid) task;
            List<FluidStack> fluidStacks =
                    QuestUtil.buildFluidStacks(fluidFactory, typedTask.requiredFluids);
            taskEntity =
                    new Task(
                            id, name, TaskType.CRAFTING, new ArrayList<>(), fluidStacks, "", 0, "");

        } else if (task instanceof TaskCheckbox) {
            taskEntity =
                    new Task(
                            id, name, TaskType.CHECKBOX, new ArrayList<>(), new ArrayList<>(),
                            "", 0, "");

        } else if (task instanceof TaskHunt) {
            TaskHunt typedTask = (TaskHunt) task;
            taskEntity =
                    new Task(
                            id, name, TaskType.HUNT, new ArrayList<>(), new ArrayList<>(),
                            typedTask.idName, typedTask.required, "");


        } else if (task instanceof TaskLocation) {
            // TODO do we need to handle the other fields in TaskLocation, like biome or structure?
            TaskLocation typedTask = (TaskLocation) task;
            taskEntity =
                    new Task(
                            id, name, TaskType.UNHANDLED, new ArrayList<>(), new ArrayList<>(),
                            "", 0, TaskLocation.getDimName(typedTask.dim));

        } else {
            // TODO add any additional task types that we need to handle here.
            logger.warn("Unhandled task type: " + task);
            taskEntity =
                    new Task(
                            id, name, TaskType.UNHANDLED, new ArrayList<>(), new ArrayList<>(),
                            "", 0, "");
        }

        return findOrPersist(Task.class, taskEntity);
    }
}
