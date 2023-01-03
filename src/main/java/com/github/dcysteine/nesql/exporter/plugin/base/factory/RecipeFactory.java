package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.proto.RecipePb;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.exporter.util.ProtoBuilder;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import jakarta.persistence.EntityManager;

import java.util.List;

public class RecipeFactory extends EntityFactory<Recipe, String> {
    public RecipeFactory(EntityManager entityManager) {
        super(entityManager);
    }

    public Recipe getRecipe(
            RecipeType recipeType,
            List<ItemGroup> itemInputs,
            List<FluidGroup> fluidInputs,
            List<ItemStack> itemOutputs,
            List<FluidStack> fluidOutputs) {
        RecipePb recipePb =
                ProtoBuilder.buildRecipePb(
                        recipeType, itemInputs, fluidInputs, itemOutputs, fluidOutputs);
        Recipe recipe =
                new Recipe(
                        IdUtil.compressProto(recipePb), recipeType,
                        itemInputs, fluidInputs, itemOutputs, fluidOutputs);
        return findOrPersist(Recipe.class, recipe);
    }
}
