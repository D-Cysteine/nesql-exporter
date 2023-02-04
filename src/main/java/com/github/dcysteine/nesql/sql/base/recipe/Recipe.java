package com.github.dcysteine.nesql.sql.base.recipe;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStackWithProbability;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStackWithProbability;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(indexes = {@Index(columnList = "RECIPE_TYPE_ID")})
@EqualsAndHashCode
@ToString
public class Recipe implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private RecipeType recipeType;

    /** Map of input index to item group. May be sparse for shaped recipes. */
    @ManyToMany
    @JoinTable(indexes = {@Index(columnList = "ITEM_INPUTS_ID")})
    private Map<Integer, ItemGroup> itemInputs;

    /** We directly include item inputs to this recipe, to speed up queries. */
    @ManyToMany
    @JoinTable(indexes = {@Index(columnList = "ITEM_INPUTS_INDEX_ID")})
    private Set<Item> itemInputsIndex;

    /** Map of input index to fluid group. May be sparse for shaped recipes. */
    @ManyToMany
    @JoinTable(indexes = {@Index(columnList = "FLUID_INPUTS_ID")})
    private Map<Integer, FluidGroup> fluidInputs;

    /** We directly include fluid inputs to this recipe, to speed up queries. */
    @ManyToMany
    @JoinTable(indexes = {@Index(columnList = "FLUID_INPUTS_INDEX_ID")})
    private Set<Fluid> fluidInputsIndex;

    /** Map of output index to item stack. May be sparse for shaped recipes. */
    @ElementCollection
    @CollectionTable(indexes = {@Index(columnList = "ITEM_OUTPUTS_VALUE_ITEM_ID")})
    private Map<Integer, ItemStackWithProbability> itemOutputs;

    /** Map of output index to fluid stack. May be sparse for shaped recipes. */
    @ElementCollection
    @CollectionTable(indexes = {@Index(columnList = "FLUID_OUTPUTS_VALUE_FLUID_ID")})
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
        this.itemInputsIndex = new HashSet<>();
        this.fluidInputs = fluidInputs;
        this.fluidInputsIndex = new HashSet<>();
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

    public Set<Item> getItemInputsIndex() {
        return itemInputsIndex;
    }

    public void addItemInputIndex(Item item) {
        itemInputsIndex.add(item);
    }

    public Map<Integer, FluidGroup> getFluidInputs() {
        return fluidInputs;
    }

    public Set<Fluid> getFluidInputsIndex() {
        return fluidInputsIndex;
    }

    public void addFluidInputIndex(Fluid fluid) {
        fluidInputsIndex.add(fluid);
    }

    public Map<Integer, ItemStackWithProbability> getItemOutputs() {
        return itemOutputs;
    }

    public Map<Integer, FluidStackWithProbability> getFluidOutputs() {
        return fluidOutputs;
    }

    @Override
    public int compareTo(Identifiable<String> other) {
        if (other instanceof Recipe) {
            return Comparator.comparing(Recipe::getRecipeType)
                    .thenComparing(Recipe::getId)
                    .compare(this, (Recipe) other);
        } else {
            return Identifiable.super.compareTo(other);
        }
    }
}
