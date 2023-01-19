package com.github.dcysteine.nesql.exporter.plugin.base.processor;

import codechicken.nei.NEIServerUtils;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.base.BasePluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.BaseRecipeType;
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
    private final RecipeType shapedCrafting;
    private final RecipeType shapelessCrafting;

    public CraftingRecipeProcessor(BasePluginExporter plugin, EntityManager entityManager) {
        this.entityManager = entityManager;
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
        RecipeBuilder builder = new RecipeBuilder(entityManager, shapedCrafting);
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
        RecipeBuilder builder = new RecipeBuilder(entityManager, shapedCrafting);
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

        RecipeBuilder builder = new RecipeBuilder(entityManager, shapelessCrafting);
        for (Object itemInput : recipe.recipeItems) {
            handleItemInput(builder, itemInput);
        }
        builder.addItemOutput(recipe.getRecipeOutput()).build();
    }

    private void processShapelessOreRecipe(ShapelessOreRecipe recipe) {
        RecipeBuilder builder = new RecipeBuilder(entityManager, shapelessCrafting);
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
