package com.github.dcysteine.nesql.exporter.plugin.minecraft;

import codechicken.nei.NEIServerUtils;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.minecraft.MinecraftRecipeTypeHandler;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.RecipeBuilder;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.Arrays;
import java.util.List;

public class CraftingRecipeProcessor extends PluginHelper {
    private final RecipeType shapedCrafting;
    private final RecipeType shapelessCrafting;

    public CraftingRecipeProcessor(
            PluginExporter exporter, MinecraftRecipeTypeHandler recipeTypeHandler) {
        super(exporter);
        this.shapedCrafting =
                recipeTypeHandler.getRecipeType(
                        MinecraftRecipeTypeHandler.MinecraftRecipeType.SHAPED_CRAFTING);
        this.shapelessCrafting =
                recipeTypeHandler.getRecipeType(
                        MinecraftRecipeTypeHandler.MinecraftRecipeType.SHAPELESS_CRAFTING);
    }

    public void process() {
        int total = CraftingManager.getInstance().getRecipeList().size();
        logger.info("Processing {} crafting recipes...", total);

        @SuppressWarnings("unchecked")
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        int count = 0;
        for (IRecipe recipe : recipes) {
            count++;

            if (recipe.getRecipeOutput() == null) {
                logger.warn("Skipping crafting recipe with null output: " + recipe);
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
                logger.warn("Unhandled crafting recipe: " + recipe);
            }

            if (Logger.intermittentLog(count)) {
                logger.info("Processed crafting recipe {} of {}", count, total);
                logger.info(
                        "Most recent recipe: {}", recipe.getRecipeOutput().getDisplayName());
            }
        }

        exporterState.flushEntityManager();
        logger.info("Finished processing crafting recipes!");
    }

    private void processShapedRecipe(ShapedRecipes recipe) {
        RecipeBuilder builder = new RecipeBuilder(exporter, shapedCrafting);
        for (Object itemInput : recipe.recipeItems) {
            if (itemInput == null) {
                builder.skipItemInput();
                continue;
            }

            handleItemInput(builder, itemInput);
        }
        builder.addItemOutput(recipe.getRecipeOutput()).build();
    }

    private void processShapedOreRecipe(ShapedOreRecipe recipe) {
        RecipeBuilder builder = new RecipeBuilder(exporter, shapedCrafting);
        for (Object itemInput : recipe.getInput()) {
            if (itemInput == null) {
                builder.skipItemInput();
                continue;
            }

            handleItemInput(builder, itemInput);
        }
        builder.addItemOutput(recipe.getRecipeOutput()).build();
    }

    private void processShapelessRecipe(ShapelessRecipes recipe) {
        // Apparently this actually happens? At least, according to a comment in NEI source.
        if (recipe.recipeItems == null) {
            logger.warn("Crafting recipe with null inputs: " + recipe);
            return;
        }

        RecipeBuilder builder = new RecipeBuilder(exporter, shapelessCrafting);
        for (Object itemInput : recipe.recipeItems) {
            handleItemInput(builder, itemInput);
        }
        builder.addItemOutput(recipe.getRecipeOutput()).build();
    }

    private void processShapelessOreRecipe(ShapelessOreRecipe recipe) {
        RecipeBuilder builder = new RecipeBuilder(exporter, shapelessCrafting);
        for (Object itemInput : recipe.getInput()) {
            handleItemInput(builder, itemInput);
        }
        builder.addItemOutput(recipe.getRecipeOutput()).build();
    }

    private void handleItemInput(RecipeBuilder builder, Object itemInput) {
        ItemStack[] itemStacks = NEIServerUtils.extractRecipeItems(itemInput);
        if (itemStacks == null || itemStacks.length == 0) {
            builder.skipItemInput();
            return;
        }

        // For some reason, a bunch of crafting recipes have stack size > 1, even though crafting
        // recipes only ever consume one item from each slot. This is probably a bug in the recipes.
        // We'll fix this by manually setting stack sizes to 1.
        ItemStack[] fixedItemStacks = new ItemStack[itemStacks.length];
        boolean foundBadStackSize = false;
        for (int i = 0; i < itemStacks.length; i++) {
            ItemStack itemStack = itemStacks[i];

            if (itemStack.stackSize != 1) {
                foundBadStackSize = true;
                fixedItemStacks[i] = itemStack.copy();
                fixedItemStacks[i].stackSize = 1;
            } else {
                fixedItemStacks[i] = itemStack;
            }
        }

        if (foundBadStackSize) {
            logger.warn("Crafting recipe with bad stack size: " + Arrays.toString(itemStacks));
        }

        builder.addItemGroupInput(fixedItemStacks);
    }
}
