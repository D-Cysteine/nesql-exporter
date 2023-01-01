package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.proto.FluidGroupPb;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.exporter.util.ProtoBuilder;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.google.common.collect.ImmutableSortedSet;
import jakarta.persistence.EntityManager;

import java.util.SortedSet;

public class FluidGroupFactory extends EntityFactory<FluidGroup, String> {
    public FluidGroupFactory(EntityManager entityManager) {
        super(entityManager);
    }

    public FluidGroup getFluidGroup(SortedSet<FluidStack> fluidStacks) {
        FluidGroupPb fluidGroupPb = ProtoBuilder.buildFluidGroupPb(fluidStacks);
        FluidGroup fluidGroup = new FluidGroup(IdUtil.compressProto(fluidGroupPb), fluidStacks);
        return findOrPersist(FluidGroup.class, fluidGroup);
    }

    public FluidGroup getFluidGroup(FluidStack fluidStack) {
        return getFluidGroup(ImmutableSortedSet.of(fluidStack));
    }
}
