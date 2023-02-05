package com.github.dcysteine.nesql.exporter.plugin.base.postprocessor;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.util.QueryUtil;

import java.util.concurrent.atomic.AtomicInteger;

/** Post-processor which resolves wildcard item stacks in item groups. */
public class WildcardItemGroupPostProcessor extends PluginHelper {
    public WildcardItemGroupPostProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void postProcess() {
        long total = QueryUtil.countWildcardItemGroups(entityManager);
        logger.info("Post-processing {} wildcard item groups...", total);

        // Need to use AtomicInteger so that we can mutate it within a lambda.
        AtomicInteger count = new AtomicInteger();
        QueryUtil.getWildcardItemGroups(entityManager).forEach(
                itemGroup -> {
                    itemGroup.getWildcardItemStacks().stream()
                            .flatMap(
                                    wildcardItemStack ->
                                            QueryUtil.resolveWildcardItemStack(
                                                    entityManager, wildcardItemStack))
                            .forEach(itemGroup::addResolvedWildcardItemStack);

                    if (Logger.intermittentLog(count.incrementAndGet())) {
                        logger.info(
                                "Post-processed wildcard item group {} of {}", count.get(), total);
                        logger.info("Most recent wildcard item group: {}", itemGroup.getId());
                    }
                });

        logger.info("Finished post-processing wildcard item groups!");
    }
}
