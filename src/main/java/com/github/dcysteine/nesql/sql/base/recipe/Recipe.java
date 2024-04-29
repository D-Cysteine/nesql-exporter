package com.github.dcysteine.nesql.sql.base.recipe;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStackWithProbability;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStackWithProbability;
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
import lombok.Getter;
import lombok.ToString;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(indexes = {@Index(columnList = "RECIPE_TYPE_ID")})
@EqualsAndHashCode
@Getter
@ToString
public class Recipe implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private RecipeType recipeType;

    /** Map of input index to item group. May be sparse for shaped recipes. */
    @ManyToMany
    private Map<Integer, ItemGroup> itemInputs;

    /** We directly include item inputs to this recipe, to speed up queries. */
    @ManyToMany
    @JoinTable(
            name = "RECIPE_ITEM_INPUTS_ITEMS",
            indexes = {@Index(columnList = "ITEM_INPUTS_ITEMS_ID")})
    private Set<Item> itemInputsItems;

    /** Map of input index to fluid group. May be sparse for shaped recipes. */
    @ManyToMany
    private Map<Integer, FluidGroup> fluidInputs;

    /** We directly include fluid inputs to this recipe, to speed up queries. */
    @ManyToMany
    @JoinTable(
            name = "RECIPE_FLUID_INPUTS_FLUIDS",
            indexes = {@Index(columnList = "FLUID_INPUTS_FLUIDS_ID")})
    private Set<Fluid> fluidInputsFluids;

    /** Map of output index to item stack with probability. May be sparse for shaped recipes. */
    @ElementCollection
    private Map<Integer, ItemStackWithProbability> itemOutputs;

    /** Map of output index to fluid stack with probability. May be sparse for shaped recipes. */
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

        itemInputsItems = new HashSet<>();
        fluidInputsFluids = new HashSet<>();
    }

    public void addItemInputsItem(Item item) {
        itemInputsItems.add(item);
    }

    public void addFluidInputsFluid(Fluid fluid) {
        fluidInputsFluids.add(fluid);
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
