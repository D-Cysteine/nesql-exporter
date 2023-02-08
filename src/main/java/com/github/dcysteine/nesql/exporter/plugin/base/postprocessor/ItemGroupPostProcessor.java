package com.github.dcysteine.nesql.exporter.plugin.base.postprocessor;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemGroupFactory;
import com.github.dcysteine.nesql.exporter.util.QueryUtil;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/** Post-processor which builds item and fluid input indices for recipes. */
public class ItemGroupPostProcessor extends PluginHelper {
    public ItemGroupPostProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void postProcess() {
        long total = QueryUtil.countItemGroups(entityManager);
        logger.info("Post-processing {} item groups...", total);

        ItemGroupFactory itemGroupFactory = new ItemGroupFactory(exporter);
        // Need to use AtomicInteger so that we can mutate it within a lambda.
        AtomicInteger count = new AtomicInteger();
        QueryUtil.getItemGroups(entityManager).forEach(
                itemGroup -> {
                    Set<ItemStack> baseItemStacks =
                            itemGroup.getItemStacks().stream()
                                    .map(itemStack -> new ItemStack(itemStack.getItem(), 1))
                                    .collect(Collectors.toCollection(HashSet::new));
                    itemGroup.setBaseItemGroup(itemGroupFactory.get(baseItemStacks));

                    if (Logger.intermittentLog(count.incrementAndGet())) {
                        logger.info("Post-processed item group {} of {}", count.get(), total);
                        logger.info("Most recent item group: {}", itemGroup.getId());
                    }
                });

        exporterState.flushEntityManager();
        logger.info("Finished post-processing item groups!");
    }
}
