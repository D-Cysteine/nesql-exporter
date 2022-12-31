package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.sql.base.recipe.FluidGroup;
import com.github.dcysteine.nesql.sql.base.recipe.FluidStack;
import jakarta.persistence.EntityManager;

import java.util.SortedSet;

public class FluidGroupFactory extends EntityFactory<FluidGroup, String> {
    public FluidGroupFactory(EntityManager entityManager) {
        super(entityManager);
    }

    public FluidGroup getFluidGroup(String id, SortedSet<FluidStack> fluidStacks) {
        FluidGroup fluidGroup = new FluidGroup(id, fluidStacks);
        return findOrPersist(FluidGroup.class, fluidGroup);
    }
}
