package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.plugin.Database;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.proto.RecipePb;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.ProtoBuilder;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStackWithProbability;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStackWithProbability;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;

import java.util.Map;

public class RecipeFactory extends EntityFactory<Recipe, String> {
    public RecipeFactory(Database database) {
        super(database);
    }

    public Recipe getRecipe(
            RecipeType recipeType,
            Map<Integer, ItemGroup> itemInputs,
            Map<Integer, FluidGroup> fluidInputs,
            Map<Integer, ItemStackWithProbability> itemOutputs,
            Map<Integer, FluidStackWithProbability> fluidOutputs) {
        RecipePb recipePb =
                ProtoBuilder.buildRecipePb(
                        recipeType, itemInputs, fluidInputs, itemOutputs, fluidOutputs);
        String id = IdPrefixUtil.RECIPE.applyPrefix(StringUtil.encodeProto(recipePb));
        Recipe recipe =
                new Recipe(id, recipeType, itemInputs, fluidInputs, itemOutputs, fluidOutputs);
        return findOrPersist(Recipe.class, recipe);
    }
}
