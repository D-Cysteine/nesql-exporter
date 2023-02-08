package com.github.dcysteine.nesql.exporter.plugin.forge.processor;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.FluidFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
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

        FluidContainerFactory fluidContainerFactory = new FluidContainerFactory(exporter);
        int count = 0;
        for (FluidContainerRegistry.FluidContainerData datum : data) {
            count++;
            fluidContainerFactory.get(datum);

            if (Logger.intermittentLog(count)) {
                logger.info("Processed Forge fluid container datum {} of {}", count, total);
                logger.info("Most recent container: {}", datum.filledContainer.getDisplayName());
            }
        }

        exporterState.flushEntityManager();
        logger.info("Finished processing Forge fluid containers!");
    }

}
