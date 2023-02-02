package com.github.dcysteine.nesql.exporter.plugin.nei;

import codechicken.nei.ItemList;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class NeiItemListProcessor extends PluginHelper {

    public NeiItemListProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        int total = ItemList.items.size();
        logger.info("Processing {} NEI items...", total);

        if (total == 0) {
            Logger.chatMessage(
                    EnumChatFormatting.RED + "NEI item list is empty; did you forget to load it?");
        }

        ItemFactory itemFactory = new ItemFactory(exporter);
        int count = 0;
        for (ItemStack itemStack : ItemList.items) {
            count++;
            try {
                itemFactory.getItem(itemStack);
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

        logger.info("Finished processing NEI items!");
    }

}
