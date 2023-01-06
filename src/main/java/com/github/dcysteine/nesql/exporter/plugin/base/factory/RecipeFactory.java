package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.proto.RecipePb;
import com.github.dcysteine.nesql.exporter.util.ProtoBuilder;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStackWithProbability;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStackWithProbability;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeInfo;
import jakarta.persistence.EntityManager;

import java.util.Map;

public class RecipeFactory extends EntityFactory<Recipe, String> {
    public RecipeFactory(EntityManager entityManager) {
        super(entityManager);
    }

    public Recipe getRecipe(
            RecipeInfo recipeInfo,
            Map<Integer, ItemGroup> itemInputs,
            Map<Integer, FluidGroup> fluidInputs,
            Map<Integer, ItemStackWithProbability> itemOutputs,
            Map<Integer, FluidStackWithProbability> fluidOutputs) {
        RecipePb recipePb =
                ProtoBuilder.buildRecipePb(
                        recipeInfo, itemInputs, fluidInputs, itemOutputs, fluidOutputs);
        Recipe recipe =
                new Recipe(
                        StringUtil.encodeProto(recipePb), recipeInfo,
                        itemInputs, fluidInputs, itemOutputs, fluidOutputs);
        return findOrPersist(Recipe.class, recipe);
    }
}
