package com.github.dcysteine.nesql.exporter.plugin.base.processor;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.FluidFactory;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Map;

public class ForgeFluidsProcessor extends PluginHelper {
    public ForgeFluidsProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        Map<String, Fluid> fluids = FluidRegistry.getRegisteredFluids();
        int total = fluids.size();
        logger.info("Processing {} Forge fluids...", total);

        FluidFactory fluidFactory = new FluidFactory(exporter);
        int count = 0;
        for (Fluid fluid : fluids.values()) {
            count++;

            FluidStack fluidStack = new FluidStack(fluid, 1);
            fluidFactory.getFluid(fluidStack);

            if (Logger.intermittentLog(count)) {
                logger.info("Processed Forge fluid {} of {}", count, total);
                logger.info("Most recent item: {}", fluidStack.getLocalizedName());
            }
        }

        logger.info("Finished processing Forge fluids!");
    }

}
