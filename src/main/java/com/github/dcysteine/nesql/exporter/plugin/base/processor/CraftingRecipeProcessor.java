package com.github.dcysteine.nesql.exporter.plugin.base.processor;

import codechicken.nei.NEIServerUtils;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.RecipeBuilder;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import jakarta.persistence.EntityManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.List;

public class CraftingRecipeProcessor {
    private final EntityManager entityManager;

    public CraftingRecipeProcessor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public void process() {
        int total = CraftingManager.getInstance().getRecipeList().size();
        Logger.MOD.info("Processing {} crafting recipes...", total);

        int count = 0;
        for (IRecipe recipe : (List<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
            count++;

            if (recipe.getRecipeOutput() == null) {
                Logger.MOD.warn("Skipping crafting recipe with null output: " + recipe);
                continue;
            }

            if (recipe instanceof ShapedRecipes) {
                processShapedRecipe((ShapedRecipes) recipe);
            } else if (recipe instanceof ShapedOreRecipe) {
                processShapedOreRecipe((ShapedOreRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipes) {
                processShapelessRecipe((ShapelessRecipes) recipe);
            } else if (recipe instanceof ShapelessOreRecipe) {
                processShapelessOreRecipe((ShapelessOreRecipe) recipe);
            } else {
                Logger.MOD.warn("Unhandled crafting recipe: " + recipe);
            }

            if (Logger.intermittentLog(count)) {
                Logger.MOD.info("Processed crafting recipe {} of {}", count, total);
                Logger.MOD.info(
                        "Most recent recipe: {}", recipe.getRecipeOutput().getDisplayName());
            }
        }

        Logger.MOD.info("Finished processing crafting recipes!");
    }

    private void processShapedRecipe(ShapedRecipes recipe) {
        RecipeBuilder builder =
                new RecipeBuilder(entityManager, RecipeType.MINECRAFT_SHAPED_CRAFTING);
        for (Object itemInput : recipe.recipeItems) {
            if (itemInput == null) {
                continue;
            }

            handleItemInput(builder, itemInput);
        }
        builder.addItemOutput(recipe.getRecipeOutput()).build();
    }

    private void processShapedOreRecipe(ShapedOreRecipe recipe) {
        RecipeBuilder builder =
                new RecipeBuilder(entityManager, RecipeType.MINECRAFT_SHAPED_CRAFTING_OREDICT);
        for (Object itemInput : recipe.getInput()) {
            if (itemInput == null) {
                continue;
            }

            handleItemInput(builder, itemInput);
        }
        builder.addItemOutput(recipe.getRecipeOutput()).build();
    }

    private void processShapelessRecipe(ShapelessRecipes recipe) {
        // Apparently this actually happens? At least, according to a comment in NEI source.
        if (recipe.recipeItems == null) {
            return;
        }

        RecipeBuilder builder =
                new RecipeBuilder(entityManager, RecipeType.MINECRAFT_SHAPELESS_CRAFTING);
        for (Object itemInput : recipe.recipeItems) {
            handleItemInput(builder, itemInput);
        }
        builder.addItemOutput(recipe.getRecipeOutput()).build();
    }

    private void processShapelessOreRecipe(ShapelessOreRecipe recipe) {
        RecipeBuilder builder = new RecipeBuilder(
                entityManager, RecipeType.MINECRAFT_SHAPELESS_CRAFTING_OREDICT);
        for (Object itemInput : recipe.getInput()) {
            handleItemInput(builder, itemInput);
        }
        builder.addItemOutput(recipe.getRecipeOutput()).build();
    }

    private void handleItemInput(RecipeBuilder builder, Object itemInput) {
        ItemStack[] itemStacks = NEIServerUtils.extractRecipeItems(itemInput);
        if (itemStacks == null || itemStacks.length == 0) {
            builder.skipItemInput();
        } else if (itemStacks.length == 1) {
            builder.addItemInput(itemStacks[0], true);
        } else {
            builder.addItemGroupInput(itemStacks, true);
        }
    }
}
