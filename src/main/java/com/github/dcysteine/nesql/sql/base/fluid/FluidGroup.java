package com.github.dcysteine.nesql.sql.base.fluid;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.Sql;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SortNatural;

import java.util.SortedSet;

/** A group of {@link FluidStack}s, all fitting into a single input slot in a recipe. */
@Entity
@EqualsAndHashCode(exclude = "recipesWithInput")  // Prevent stack overflow
@ToString
public class FluidGroup implements Identifiable<String> {
    @Id
    @Column(length = Sql.STRING_MAX_LENGTH, nullable = false)
    private String id;

    @ElementCollection
    @SortNatural
    private SortedSet<FluidStack> fluidStacks;

    @ManyToMany(mappedBy = "fluidInputs")
    @SortNatural
    private SortedSet<Recipe> recipesWithInput;

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

    public SortedSet<Recipe> getRecipesWithInput() {
        return recipesWithInput;
    }
}
