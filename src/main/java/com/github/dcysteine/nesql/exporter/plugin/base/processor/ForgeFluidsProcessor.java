package com.github.dcysteine.nesql.exporter.plugin.base.processor;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.Database;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.FluidFactory;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Map;

public class ForgeFluidsProcessor {
    private final Database database;

    public ForgeFluidsProcessor(Database database) {
        this.database = database;
    }

    public void process() {
        Map<String, Fluid> fluids = FluidRegistry.getRegisteredFluids();
        int total = fluids.size();
        Logger.BASE.info("Processing {} Forge fluids...", total);

        FluidFactory fluidFactory = new FluidFactory(database);
        int count = 0;
        for (Fluid fluid : fluids.values()) {
            count++;

            FluidStack fluidStack = new FluidStack(fluid, 1);
            fluidFactory.getFluid(fluidStack);

            if (Logger.intermittentLog(count)) {
                Logger.BASE.info("Processed Forge fluid {} of {}", count, total);
                Logger.BASE.info("Most recent item: {}", fluidStack.getLocalizedName());
            }
        }

        Logger.BASE.info("Finished processing Forge fluids!");
    }

}
