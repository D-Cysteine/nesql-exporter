package com.github.dcysteine.nesql.sql.base.fluid;

import com.github.dcysteine.nesql.sql.Identifiable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

/** A group of {@link FluidStack}s, all fitting into a single input slot in a recipe. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class FluidGroup implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ElementCollection
    @CollectionTable(indexes = {@Index(columnList = "FLUID_STACKS_FLUID_ID")})
    private Set<FluidStack> fluidStacks;

    /** Needed by Hibernate. */
    protected FluidGroup() {}

    public FluidGroup(String id, Set<FluidStack> fluidStacks) {
        this.id = id;
        this.fluidStacks = fluidStacks;
    }
}
