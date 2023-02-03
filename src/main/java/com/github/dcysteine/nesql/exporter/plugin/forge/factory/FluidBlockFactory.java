package com.github.dcysteine.nesql.exporter.plugin.forge.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.forge.FluidBlock;

public class FluidBlockFactory extends EntityFactory<FluidBlock, String> {

    public FluidBlockFactory(PluginExporter exporter) {
        super(exporter);
    }

    public FluidBlock get(Fluid fluid, Item block) {
        String id = IdPrefixUtil.FLUID_BLOCK.applyPrefix(fluid.getId(), block.getId());

        FluidBlock fluidBlock = new FluidBlock(id, fluid, block);
        return findOrPersist(FluidBlock.class, fluidBlock);
    }
}
