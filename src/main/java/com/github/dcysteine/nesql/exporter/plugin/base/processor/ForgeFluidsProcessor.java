package com.github.dcysteine.nesql.exporter.plugin.base.processor;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.FluidFactory;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import jakarta.persistence.EntityManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Map;

public class ForgeFluidsProcessor {
    private final EntityManager entityManager;

    public ForgeFluidsProcessor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void process() {
        Map<String, net.minecraftforge.fluids.Fluid> fluids = FluidRegistry.getRegisteredFluids();
        int total = fluids.size();
        Logger.MOD.info("Processing {} Forge fluids...", total);

        FluidFactory fluidFactory = new FluidFactory(entityManager);
        int count = 0;
        for (net.minecraftforge.fluids.Fluid fluid : fluids.values()) {
            count++;

            FluidStack fluidStack = new FluidStack(fluid, 1);
            fluidFactory.findOrPersist(Fluid.class, fluidFactory.getFluid(fluidStack));

            if (Logger.intermittentLog(count)) {
                Logger.MOD.info("Processed Forge fluid {} of {}", count, total);
                Logger.MOD.info("Most recent item: {}", fluidStack.getLocalizedName());
            }
        }

        Logger.MOD.info("Finished processing Forge fluids!");
    }

}
