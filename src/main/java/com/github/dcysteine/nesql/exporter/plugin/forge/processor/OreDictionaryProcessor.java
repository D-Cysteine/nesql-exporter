package com.github.dcysteine.nesql.exporter.plugin.forge.processor;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.forge.factory.OreDictionaryFactory;
import net.minecraft.item.ItemStack;

import java.util.List;

public class OreDictionaryProcessor extends PluginHelper {

    public OreDictionaryProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        String[] names = net.minecraftforge.oredict.OreDictionary.getOreNames();
        int total = names.length;
        logger.info("Processing {} ore dictionary entries...", total);

        OreDictionaryFactory oreDictionaryFactory = new OreDictionaryFactory(exporter);
        int count = 0;
        for (String name : names) {
            count++;

            List<ItemStack> itemStacks =
                    net.minecraftforge.oredict.OreDictionary.getOres(name, false);
            oreDictionaryFactory.get(name, itemStacks);

            if (Logger.intermittentLog(count)) {
                logger.info("Processed ore dictionary entry {} of {}", count, total);
                logger.info("Most recent entry: {}", name);
            }
        }

        logger.info("Finished processing ore dictionary!");
    }

}
