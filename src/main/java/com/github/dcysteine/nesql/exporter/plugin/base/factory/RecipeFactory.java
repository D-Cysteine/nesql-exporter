package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.sql.base.recipe.FluidGroup;
import com.github.dcysteine.nesql.sql.base.recipe.FluidStack;
import com.github.dcysteine.nesql.sql.base.recipe.ItemGroup;
import com.github.dcysteine.nesql.sql.base.recipe.ItemStack;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import jakarta.persistence.EntityManager;

import java.util.List;

public class RecipeFactory extends EntityFactory<Recipe, String> {
    public RecipeFactory(EntityManager entityManager) {
        super(entityManager);
    }

    public Recipe getRecipe(
            String id,
            RecipeType recipeType,
            List<ItemGroup> itemInputs,
            List<FluidGroup> fluidInputs,
            List<ItemStack> itemOutputs,
            List<FluidStack> fluidOutputs) {
        Recipe recipe =
                new Recipe(id, recipeType, itemInputs, fluidInputs, itemOutputs, fluidOutputs);
        return findOrPersist(Recipe.class, recipe);
    }
}
