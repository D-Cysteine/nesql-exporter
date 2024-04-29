package com.github.dcysteine.nesql.exporter.plugin.nei;

import codechicken.nei.ItemList;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import net.minecraft.item.ItemStack;

public class NeiItemListProcessor extends PluginHelper {

    public NeiItemListProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        int total = ItemList.items.size();
        logger.info("Processing {} NEI items...", total);

        ItemFactory itemFactory = new ItemFactory(exporter);
        int count = 0;
        for (ItemStack itemStack : ItemList.items) {
            count++;
            try {
                itemFactory.get(itemStack);
            } catch (Exception e) {
                // GTNH has some bad items, so we have to do this =(
                // For whatever reason, the exceptions thrown by those items don't even have stack
                // traces!
                logger.info("Found a bad item: " + itemStack.getDisplayName());
                e.printStackTrace();
            }

            if (Logger.intermittentLog(count)) {
                logger.info("Processed NEI item {} of {}", count, total);
                logger.info("Most recent item: {}", itemStack.getDisplayName());
            }
        }

        exporterState.flushEntityManager();
        logger.info("Finished processing NEI items!");
    }

}
