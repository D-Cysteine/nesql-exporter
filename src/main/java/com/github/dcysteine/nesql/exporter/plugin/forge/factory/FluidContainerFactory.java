package com.github.dcysteine.nesql.exporter.plugin.forge.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.forge.FluidContainer;

import java.util.Map;

public class FluidContainerFactory extends EntityFactory<FluidContainer, String> {
    public FluidContainerFactory(PluginExporter exporter) {
        super(exporter);
    }

    public FluidContainer get(Fluid fluid, Map<Item, Integer> containers) {
        String id = IdPrefixUtil.FLUID_CONTAINER.applyPrefix(fluid.getId());
        FluidContainer fluidContainer = new FluidContainer(id, fluid, containers);
        return findOrPersist(FluidContainer.class, fluidContainer);
    }
}
