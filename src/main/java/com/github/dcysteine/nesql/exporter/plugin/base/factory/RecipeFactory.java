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
        RecipePb.Builder builder = RecipePb.newBuilder()
                .setRecipeType(recipeType.ordinal());
        itemInputs.forEach(
                itemGroup -> builder.addItemInput(ProtoBuilder.buildItemGroupPb(itemGroup)));
        fluidInputs.forEach(
                fluidGroup -> builder.addFluidInput(ProtoBuilder.buildFluidGroupPb(fluidGroup)));
        itemOutputs.forEach(
                itemStack -> builder.addItemOutput(ProtoBuilder.buildItemPb(itemStack)));
        fluidOutputs.forEach(
                fluidStack -> builder.addFluidOutput(ProtoBuilder.buildFluidPb(fluidStack)));

        Recipe recipe =
                new Recipe(
                        IdUtil.compressProto(builder.build()), recipeType,
                        itemInputs, fluidInputs, itemOutputs, fluidOutputs);
        return findOrPersist(Recipe.class, recipe);
    }
}
