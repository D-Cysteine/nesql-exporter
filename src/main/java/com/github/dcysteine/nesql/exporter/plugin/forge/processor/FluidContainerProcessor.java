package com.github.dcysteine.nesql.exporter.plugin.forge.processor;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.FluidFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.plugin.forge.factory.EmptyContainerFactory;
import com.github.dcysteine.nesql.exporter.plugin.forge.factory.FluidContainerFactory;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class FluidContainerProcessor extends PluginHelper {
    public FluidContainerProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        FluidContainerRegistry.FluidContainerData[] data =
                FluidContainerRegistry.getRegisteredFluidContainerData();
        int total = data.length;
        logger.info("Processing {} Forge fluid container data...", total);

        FluidFactory fluidFactory = new FluidFactory(exporter);
        ItemFactory itemFactory = new ItemFactory(exporter);
        Table<Fluid, Item, Integer> fluidContainers = HashBasedTable.create();
        Table<Item, Item, Integer> emptyContainers = HashBasedTable.create();
        int count = 0;
        for (FluidContainerRegistry.FluidContainerData datum : data) {
            count++;

            int capacity = datum.fluid.amount;
            Fluid fluid = fluidFactory.get(datum.fluid);
            Item filledContainer = itemFactory.get(datum.filledContainer);
            Item emptyContainer = itemFactory.get(datum.emptyContainer);

            fluidContainers.put(fluid, filledContainer, capacity);
            emptyContainers.put(emptyContainer, filledContainer, capacity);

            if (Logger.intermittentLog(count)) {
                logger.info("Processed Forge fluid container datum {} of {}", count, total);
                logger.info("Most recent container: {}", filledContainer.getLocalizedName());
            }
        }

        FluidContainerFactory fluidContainerFactory = new FluidContainerFactory(exporter);
        for (Fluid fluid : fluidContainers.rowKeySet()) {
            fluidContainerFactory.get(fluid, fluidContainers.row(fluid));
        }

        EmptyContainerFactory emptyContainerFactory = new EmptyContainerFactory(exporter);
        for (Item emptyContainer : emptyContainers.rowKeySet()) {
            emptyContainerFactory.get(emptyContainer, emptyContainers.row(emptyContainer));
        }

        logger.info("Finished processing Forge fluid containers!");
    }

}
