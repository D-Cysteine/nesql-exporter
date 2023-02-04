package com.github.dcysteine.nesql.exporter.plugin.base.postprocessor;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.util.QueryUtil;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;

import java.util.List;

/** Post-processor which resolves wildcard item stacks in item groups. */
public class WildcardItemGroupPostProcessor extends PluginHelper {
    public WildcardItemGroupPostProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void postProcess() {
        List<ItemGroup> itemGroups = QueryUtil.getWildcardItemGroups(entityManager);
        int total = itemGroups.size();
        logger.info("Post-processing {} wildcard item groups...", total);

        int count = 0;
        for (ItemGroup itemGroup : itemGroups) {
            count++;

            itemGroup.getWildcardItemStacks().stream()
                    .map(
                            wildcardItemStack ->
                                    QueryUtil.resolveWildcardItemStack(
                                            entityManager, wildcardItemStack))
                    .forEach(itemGroup::addAllResolvedWildcardItemStacks);

            if (Logger.intermittentLog(count)) {
                logger.info("Post-processed wildcard item group {} of {}", count, total);
                logger.info("Most recent wildcard item group: {}", itemGroup.getId());
            }
        }

        logger.info("Finished post-processing wildcard item groups!");
    }
}
