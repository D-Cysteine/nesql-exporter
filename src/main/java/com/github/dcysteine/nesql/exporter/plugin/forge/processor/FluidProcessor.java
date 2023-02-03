package com.github.dcysteine.nesql.exporter.plugin.forge.processor;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.FluidFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.plugin.forge.factory.FluidBlockFactory;
import com.github.dcysteine.nesql.exporter.util.ItemUtil;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Map;
import java.util.Optional;

public class FluidProcessor extends PluginHelper {
    public FluidProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        Map<String, net.minecraftforge.fluids.Fluid> fluids = FluidRegistry.getRegisteredFluids();
        int total = fluids.size();
        logger.info("Processing {} Forge fluids...", total);

        FluidFactory fluidFactory = new FluidFactory(exporter);
        ItemFactory itemFactory = new ItemFactory(exporter);
        FluidBlockFactory fluidBlockFactory = new FluidBlockFactory(exporter);
        int count = 0;
        for (net.minecraftforge.fluids.Fluid fluid : fluids.values()) {
            count++;

            Fluid fluidEntity = fluidFactory.get(fluid);
            Optional<ItemStack> block = ItemUtil.getItemStack(fluid);
            if (block.isPresent()) {
                Item item = itemFactory.get(block.get());
                fluidBlockFactory.get(fluidEntity, item);
            }

            if (Logger.intermittentLog(count)) {
                logger.info("Processed Forge fluid {} of {}", count, total);
                logger.info("Most recent item: {}", fluidEntity.getLocalizedName());
            }
        }

        logger.info("Finished processing Forge fluids!");
    }

}
