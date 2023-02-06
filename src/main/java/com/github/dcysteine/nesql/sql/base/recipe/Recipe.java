package com.github.dcysteine.nesql.sql.base.recipe;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStackWithProbability;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
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
    private Map<Integer, ItemGroup> itemInputs;

    /**
     * Set of unique input item groups.
     *
     * <p>For whatever reason, it seems that JPA Specification does not support both retrieving
     * distinct results and sorting, so we must include a copy of {@link #itemInputs} that doesn't
     * have duplicate entries in order to have both.
     */
    @ManyToMany
    @JoinTable(indexes = {@Index(columnList = "UNIQUE_ITEM_INPUTS_ID")})
    private Set<ItemGroup> uniqueItemInputs;

    /** We directly include item inputs to this recipe, to speed up queries. */
    @ManyToMany
    @JoinTable(indexes = {@Index(columnList = "ITEM_INPUTS_ITEMS_ID")})
    private Set<Item> itemInputsItems;

    /** Map of input index to fluid group. May be sparse for shaped recipes. */
    @ManyToMany
    @JoinTable(indexes = {@Index(columnList = "FLUID_INPUTS_ID")})
    private Map<Integer, FluidGroup> fluidInputs;

    /**
     * Set of unique input fluid groups.
     *
     * <p>For whatever reason, it seems that JPA Specification does not support both retrieving
     * distinct results and sorting, so we must include a copy of {@link #fluidInputs} that doesn't
     * have duplicate entries in order to have both.
     */
    @ManyToMany
    @JoinTable(indexes = {@Index(columnList = "UNIQUE_FLUID_INPUTS_ID")})
    private Set<FluidGroup> uniqueFluidInputs;

    /** We directly include fluid inputs to this recipe, to speed up queries. */
    @ManyToMany
    @JoinTable(indexes = {@Index(columnList = "FLUID_INPUTS_FLUIDS_ID")})
    private Set<Fluid> fluidInputsFluids;

    /** Map of output index to item stack with probability. May be sparse for shaped recipes. */
    @ElementCollection
    private Map<Integer, ItemStackWithProbability> itemOutputs;

    /**
     * Set of unique output items.
     *
     * <p>For whatever reason, it seems that JPA Specification does not support both retrieving
     * distinct results and sorting, so we must include a copy of {@link #itemOutputs} that doesn't
     * have duplicate entries in order to have both.
     */
    @ManyToMany
    @JoinTable(indexes = {@Index(columnList = "UNIQUE_ITEM_OUTPUTS_ID")})
    private Set<Item> uniqueItemOutputs;

    /** Map of output index to fluid stack with probability. May be sparse for shaped recipes. */
    @ElementCollection
    private Map<Integer, FluidStackWithProbability> fluidOutputs;

    /**
     * Set of unique output fluids.
     *
     * <p>For whatever reason, it seems that JPA Specification does not support both retrieving
     * distinct results and sorting, so we must include a copy of {@link #fluidOutputs} that doesn't
     * have duplicate entries in order to have both.
     */
    @ManyToMany
    @JoinTable(indexes = {@Index(columnList = "UNIQUE_FLUID_OUTPUTS_ID")})
    private Set<Fluid> uniqueFluidOutputs;

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

        uniqueItemInputs = new HashSet<>();
        uniqueItemInputs.addAll(itemInputs.values());
        itemInputsItems = new HashSet<>();
        uniqueFluidInputs = new HashSet<>();
        uniqueFluidInputs.addAll(fluidInputs.values());
        fluidInputsFluids = new HashSet<>();

        uniqueItemOutputs = new HashSet<>();
        itemOutputs.values().stream()
                .map(ItemStackWithProbability::getItem)
                .forEach(uniqueItemOutputs::add);
        uniqueFluidOutputs = new HashSet<>();
        fluidOutputs.values().stream()
                .map(FluidStackWithProbability::getFluid)
                .forEach(uniqueFluidOutputs::add);
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

    public Set<ItemGroup> getUniqueItemInputs() {
        return uniqueItemInputs;
    }

    public Set<Item> getItemInputsItems() {
        return itemInputsItems;
    }

    public void addItemInputsItem(Item item) {
        itemInputsItems.add(item);
    }

    public Map<Integer, FluidGroup> getFluidInputs() {
        return fluidInputs;
    }

    public Set<FluidGroup> getUniqueFluidInputs() {
        return uniqueFluidInputs;
    }

    public Set<Fluid> getFluidInputsFluids() {
        return fluidInputsFluids;
    }

    public void addFluidInputsFluid(Fluid fluid) {
        fluidInputsFluids.add(fluid);
    }

    public Map<Integer, ItemStackWithProbability> getItemOutputs() {
        return itemOutputs;
    }

    public Set<Item> getUniqueItemOutputs() {
        return uniqueItemOutputs;
    }

    public Map<Integer, FluidStackWithProbability> getFluidOutputs() {
        return fluidOutputs;
    }

    public Set<Fluid> getUniqueFluidOutputs() {
        return uniqueFluidOutputs;
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
