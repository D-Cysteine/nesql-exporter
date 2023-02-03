package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.proto.FluidGroupPb;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.ProtoBuilder;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FluidGroupFactory extends EntityFactory<FluidGroup, String> {
    private final FluidFactory fluidFactory;

    public FluidGroupFactory(PluginExporter exporter) {
        super(exporter);
        fluidFactory = new FluidFactory(exporter);
    }

    public FluidGroup get(net.minecraftforge.fluids.FluidStack fluidStack) {
        return get(ImmutableList.of(fluidStack));
    }

    public FluidGroup get(Collection<net.minecraftforge.fluids.FluidStack> fluidStacks) {
        Set<FluidStack> fluidStackEntities =
                fluidStacks.stream()
                        .filter(Objects::nonNull)
                        .map(this::buildFluidStack)
                        .collect(Collectors.toCollection(HashSet::new));

        FluidGroupPb fluidGroupPb = ProtoBuilder.buildFluidGroupPb(fluidStackEntities);
        String id = IdPrefixUtil.FLUID_GROUP.applyPrefix(StringUtil.encodeProto(fluidGroupPb));
        FluidGroup fluidGroup = new FluidGroup(id, fluidStackEntities);
        return findOrPersist(FluidGroup.class, fluidGroup);
    }

    private FluidStack buildFluidStack(net.minecraftforge.fluids.FluidStack fluidStack) {
        return new FluidStack(fluidFactory.get(fluidStack), fluidStack.amount);
    }
}
