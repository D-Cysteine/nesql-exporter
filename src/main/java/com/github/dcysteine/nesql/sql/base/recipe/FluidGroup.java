package com.github.dcysteine.nesql.sql.base.recipe;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.Sql;
import com.github.dcysteine.nesql.sql.base.Fluid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.Set;

/** A group of {@link FluidStack}s, all fitting into a single input slot in a recipe. */
@Entity
public class FluidGroup extends Identifiable<String> {
    @Id
    @Column(length = Sql.STRING_MAX_LENGTH)
    private String id;

    @ManyToMany(targetEntity = Fluid.class)
    private Set<FluidStack> fluidStacks;

    /** Needed by Hibernate. */
    protected FluidGroup() {}

    public FluidGroup(String id, Set<FluidStack> fluidStacks) {
        this.id = id;
        this.fluidStacks = fluidStacks;
    }

    @Override
    public String getId() {
        return id;
    }

    public Set<FluidStack> getFluidStacks() {
        return fluidStacks;
    }
}
