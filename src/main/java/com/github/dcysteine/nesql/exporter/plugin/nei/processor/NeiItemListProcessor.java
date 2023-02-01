package com.github.dcysteine.nesql.exporter.plugin.nei.processor;

import codechicken.nei.ItemList;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.Database;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class NeiItemListProcessor {
    private final Database database;

    public NeiItemListProcessor(Database database) {
        this.database = database;
    }

    public void process() {
        int total = ItemList.items.size();
        Logger.BASE.info("Processing {} NEI items...", total);

        if (total == 0) {
            Logger.chatMessage(
                    EnumChatFormatting.RED + "NEI item list is empty; did you forget to load it?");
        }

        ItemFactory itemFactory = new ItemFactory(database);
        int count = 0;
        for (ItemStack itemStack : ItemList.items) {
            count++;
            try {
                itemFactory.getItem(itemStack);
            } catch (Exception e) {
                // GTNH has some bad items, so we have to do this =(
                // For whatever reason, the exceptions thrown by those items don't even have stack
                // traces!
                Logger.BASE.info("Found a bad item: " + itemStack.getDisplayName());
                e.printStackTrace();
            }

            if (Logger.intermittentLog(count)) {
                Logger.BASE.info("Processed NEI item {} of {}", count, total);
                Logger.BASE.info("Most recent item: {}", itemStack.getDisplayName());
            }
        }

        Logger.BASE.info("Finished processing NEI items!");
    }

}
