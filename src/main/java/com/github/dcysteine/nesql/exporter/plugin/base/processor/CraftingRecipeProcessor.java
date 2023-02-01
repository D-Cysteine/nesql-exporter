package com.github.dcysteine.nesql.exporter.plugin.base.processor;

import codechicken.nei.NEIServerUtils;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.Database;
import com.github.dcysteine.nesql.exporter.plugin.base.BasePluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.BaseRecipeType;
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

public class CraftingRecipeProcessor {
    private final Database database;
    private final RecipeType shapedCrafting;
    private final RecipeType shapelessCrafting;

    public CraftingRecipeProcessor(BasePluginExporter plugin, Database database) {
        this.database = database;
        this.shapedCrafting = plugin.getRecipeType(BaseRecipeType.SHAPED_CRAFTING);
        this.shapelessCrafting = plugin.getRecipeType(BaseRecipeType.SHAPELESS_CRAFTING);
    }

    public void process() {
        int total = CraftingManager.getInstance().getRecipeList().size();
        Logger.BASE.info("Processing {} crafting recipes...", total);

        @SuppressWarnings("unchecked")
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        int count = 0;
        for (IRecipe recipe : recipes) {
            count++;

            if (recipe.getRecipeOutput() == null) {
                Logger.BASE.warn("Skipping crafting recipe with null output: " + recipe);
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
                Logger.BASE.warn("Unhandled crafting recipe: " + recipe);
            }

            if (Logger.intermittentLog(count)) {
                Logger.BASE.info("Processed crafting recipe {} of {}", count, total);
                Logger.BASE.info(
                        "Most recent recipe: {}", recipe.getRecipeOutput().getDisplayName());
            }
        }

        Logger.BASE.info("Finished processing crafting recipes!");
    }

    private void processShapedRecipe(ShapedRecipes recipe) {
        RecipeBuilder builder = new RecipeBuilder(database, shapedCrafting);
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
        RecipeBuilder builder = new RecipeBuilder(database, shapedCrafting);
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
            Logger.BASE.warn("Crafting recipe with null inputs: " + recipe);
            return;
        }

        RecipeBuilder builder = new RecipeBuilder(database, shapelessCrafting);
        for (Object itemInput : recipe.recipeItems) {
            handleItemInput(builder, itemInput);
        }
        builder.addItemOutput(recipe.getRecipeOutput()).build();
    }

    private void processShapelessOreRecipe(ShapelessOreRecipe recipe) {
        RecipeBuilder builder = new RecipeBuilder(database, shapelessCrafting);
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
            Logger.BASE.warn("Crafting recipe with bad stack size: " + Arrays.toString(itemStacks));
        }

        builder.addItemGroupInput(fixedItemStacks, true);
    }
}
