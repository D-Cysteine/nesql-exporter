package com.github.dcysteine.nesql.exporter.plugin.forge.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.FluidFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.forge.FluidContainer;
import net.minecraftforge.fluids.FluidContainerRegistry;

import java.util.Map;

public class FluidContainerFactory extends EntityFactory<FluidContainer, String> {
    private final FluidFactory fluidFactory;
    private final ItemFactory itemFactory;

    public FluidContainerFactory(PluginExporter exporter) {
        super(exporter);
        fluidFactory = new FluidFactory(exporter);
        itemFactory = new ItemFactory(exporter);
    }

    public FluidContainer get(FluidContainerRegistry.FluidContainerData datum) {
        Fluid fluid = fluidFactory.get(datum.fluid);
        Item container = itemFactory.get(datum.filledContainer);
        Item emptyContainer = itemFactory.get(datum.emptyContainer);

        String id = IdPrefixUtil.FLUID_CONTAINER.applyPrefix(container.getId());
        FluidStack fluidStack = new FluidStack(fluid, datum.fluid.amount);

        FluidContainer fluidContainer =
                new FluidContainer(id, fluidStack, container, emptyContainer);
        return findOrPersist(FluidContainer.class, fluidContainer);
    }
}
