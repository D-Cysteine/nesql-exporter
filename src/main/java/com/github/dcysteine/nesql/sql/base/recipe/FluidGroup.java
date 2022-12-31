package com.github.dcysteine.nesql.sql.base.recipe;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.Sql;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SortNatural;

import java.util.SortedSet;

/** A group of {@link FluidStack}s, all fitting into a single input slot in a recipe. */
@Entity
@EqualsAndHashCode
public class FluidGroup implements Identifiable<String> {
    @Id
    @Column(length = Sql.STRING_MAX_LENGTH)
    private String id;

    @ElementCollection
    @SortNatural
    private SortedSet<FluidStack> fluidStacks;

    /** Needed by Hibernate. */
    protected FluidGroup() {}

    public FluidGroup(String id, SortedSet<FluidStack> fluidStacks) {
        this.id = id;
        this.fluidStacks = fluidStacks;
    }

    @Override
    public String getId() {
        return id;
    }

    public SortedSet<FluidStack> getFluidStacks() {
        return fluidStacks;
    }
}
