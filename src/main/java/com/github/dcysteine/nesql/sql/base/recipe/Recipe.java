package com.github.dcysteine.nesql.sql.base.recipe;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.Sql;
import com.github.dcysteine.nesql.sql.base.Fluid;
import com.github.dcysteine.nesql.sql.base.Item;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OrderColumn;

import java.util.List;

@Entity
public class Recipe extends Identifiable<String> {
    @Id
    @Column(length = Sql.STRING_MAX_LENGTH)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecipeType recipeType;

    /**
     * Ordered list of item inputs. May contain null for shaped recipes.
     *
     * <p>To be clear: it is okay for entries in this list to be null; it is NOT okay for them to be
     * an empty {@link ItemGroup}! If this happens, it is a sign of a malformed recipe.
     */
    @ManyToMany
    @OrderColumn
    private List<ItemGroup> itemInputs;

    /**
     * Ordered list of fluid inputs. May contain null for shaped recipes.
     *
     * <p>To be clear: it is okay for entries in this list to be null; it is NOT okay for them to be
     * an empty {@link FluidGroup}! If this happens, it is a sign of a malformed recipe.
     */
    @ManyToMany
    @OrderColumn
    private List<FluidGroup> fluidInputs;

    @ElementCollection(targetClass = Item.class)
    @OrderColumn
    private List<ItemStack> itemOutputs;

    @ElementCollection(targetClass = Fluid.class)
    @OrderColumn
    private List<FluidStack> fluidOutputs;

    /** Needed by Hibernate. */
    protected Recipe() {}

    public Recipe(
            String id,
            RecipeType recipeType,
            List<ItemGroup> itemInputs,
            List<FluidGroup> fluidInputs,
            List<ItemStack> itemOutputs,
            List<FluidStack> fluidOutputs) {
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

    public List<ItemGroup> getItemInputs() {
        return itemInputs;
    }

    public List<FluidGroup> getFluidInputs() {
        return fluidInputs;
    }

    public List<ItemStack> getItemOutputs() {
        return itemOutputs;
    }

    public List<FluidStack> getFluidOutputs() {
        return fluidOutputs;
    }
}
