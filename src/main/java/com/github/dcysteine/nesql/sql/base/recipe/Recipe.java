package com.github.dcysteine.nesql.sql.base.recipe;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStackWithProbability;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStackWithProbability;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

@Entity
@EqualsAndHashCode
@ToString
public class Recipe implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private RecipeType recipeType;

    /** Map of input index to item group. May be sparse for shaped recipes. */
    @ManyToMany
    private Map<Integer, ItemGroup> itemInputs;

    /** Map of input index to fluid group. May be sparse for shaped recipes. */
    @ManyToMany
    private Map<Integer, FluidGroup> fluidInputs;

    /** Map of output index to item stack. May be sparse for shaped recipes. */
    @ElementCollection
    private Map<Integer, ItemStackWithProbability> itemOutputs;

    /** Map of output index to fluid stack. May be sparse for shaped recipes. */
    @ElementCollection
    private Map<Integer, FluidStackWithProbability> fluidOutputs;

    /** Needed by Hibernate. */
    protected Recipe() {}

    public Recipe(
            String id,
            RecipeType recipeType,
            Map<Integer, ItemGroup> itemInputs,
            Map<Integer, FluidGroup> fluidInputs,
            Map<Integer, ItemStackWithProbability> itemOutputs,
            Map<Integer, FluidStackWithProbability> fluidOutputs) {
        this.id = id;
        this.recipeType = recipeType;
        this.itemInputs = itemInputs;
        this.fluidInputs = fluidInputs;
        this.itemOutputs = itemOutputs;
        this.fluidOutputs = fluidOutputs;
    }

    @Override
    public String getId() {
        return id;
    }

    public RecipeType getRecipeType() {
        return recipeType;
    }

    public Map<Integer, ItemGroup> getItemInputs() {
        return itemInputs;
    }

    public Map<Integer, FluidGroup> getFluidInputs() {
        return fluidInputs;
    }

    public Map<Integer, ItemStackWithProbability> getItemOutputs() {
        return itemOutputs;
    }

    public Map<Integer, FluidStackWithProbability> getFluidOutputs() {
        return fluidOutputs;
    }
}
