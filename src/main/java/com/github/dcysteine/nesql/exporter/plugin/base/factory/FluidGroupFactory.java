package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.plugin.Database;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.proto.FluidGroupPb;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.ProtoBuilder;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.google.common.collect.ImmutableSortedSet;

import java.util.SortedSet;

public class FluidGroupFactory extends EntityFactory<FluidGroup, String> {
    public FluidGroupFactory(Database database) {
        super(database);
    }

    public FluidGroup getFluidGroup(SortedSet<FluidStack> fluidStacks) {
        FluidGroupPb fluidGroupPb = ProtoBuilder.buildFluidGroupPb(fluidStacks);
        String id = IdPrefixUtil.FLUID_GROUP.applyPrefix(StringUtil.encodeProto(fluidGroupPb));
        FluidGroup fluidGroup = new FluidGroup(id, fluidStacks);
        return findOrPersist(FluidGroup.class, fluidGroup);
    }

    public FluidGroup getFluidGroup(FluidStack fluidStack) {
        return getFluidGroup(ImmutableSortedSet.of(fluidStack));
    }
}
