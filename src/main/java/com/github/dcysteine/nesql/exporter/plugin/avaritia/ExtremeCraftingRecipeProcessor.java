package com.github.dcysteine.nesql.exporter.plugin.avaritia;

import codechicken.nei.NEIServerUtils;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.RecipeBuilder;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import fox.spiteful.avaritia.crafting.ExtremeShapedOreRecipe;
import fox.spiteful.avaritia.crafting.ExtremeShapedRecipe;
import fox.spiteful.avaritia.crafting.ExtremeShapelessRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.Arrays;
import java.util.List;

/**
 * Has almost identical logic to
 * {@link com.github.dcysteine.nesql.exporter.plugin.minecraft.CraftingRecipeProcessor}.
 *
 * <p>Many of the checks and logged warnings are probably not actually needed for Avaritia recipes.
 */
public class ExtremeCraftingRecipeProcessor extends PluginHelper {
    private final RecipeType shapedExtremeCrafting;
    private final RecipeType shapelessExtremeCrafting;

    public ExtremeCraftingRecipeProcessor(
            PluginExporter exporter, AvaritiaRecipeTypeHandler recipeTypeHandler) {
        super(exporter);
        this.shapedExtremeCrafting =
                recipeTypeHandler.getRecipeType(
                        AvaritiaRecipeTypeHandler.AvaritiaRecipeType.SHAPED_EXTREME_CRAFTING);
        this.shapelessExtremeCrafting =
                recipeTypeHandler.getRecipeType(
                        AvaritiaRecipeTypeHandler.AvaritiaRecipeType.SHAPELESS_EXTREME_CRAFTING);
    }

    public void process() {
        @SuppressWarnings("unchecked")
        List<IRecipe> recipes = ExtremeCraftingManager.getInstance().getRecipeList();
        int total = recipes.size();
        logger.info("Processing {} Avaritia extreme crafting recipes...", total);

        int count = 0;
        for (IRecipe recipe : recipes) {
            count++;

            if (recipe instanceof ExtremeShapedRecipe) {
                processShapedRecipe((ExtremeShapedRecipe) recipe);
            } else if (recipe instanceof ExtremeShapedOreRecipe) {
                processShapedOreRecipe((ExtremeShapedOreRecipe) recipe);
            } else if (recipe instanceof ExtremeShapelessRecipe) {
                processShapelessRecipe((ExtremeShapelessRecipe) recipe);
            } else if (recipe instanceof ShapelessOreRecipe) {
                processShapelessOreRecipe((ShapelessOreRecipe) recipe);
            } else {
                logger.warn("Unhandled Avaritia extreme crafting recipe: {}", recipe);
            }

            if (Logger.intermittentLog(count)) {
                logger.info("Processed Avaritia extreme crafting recipe {} of {}", count, total);
                logger.info(
                        "Most recent recipe: {}", recipe.getRecipeOutput().getDisplayName());
            }
        }

        exporterState.flushEntityManager();
        logger.info("Finished processing Avaritia extreme crafting recipes!");
    }

    private void processShapedRecipe(ExtremeShapedRecipe recipe) {
        RecipeBuilder builder = new RecipeBuilder(exporter, shapedExtremeCrafting);
        for (Object itemInput : recipe.recipeItems) {
            if (itemInput == null) {
                builder.skipItemInput();
                continue;
            }

            handleItemInput(builder, itemInput);
        }
        builder.addItemOutput(recipe.getRecipeOutput()).build();
    }

    private void processShapedOreRecipe(ExtremeShapedOreRecipe recipe) {
        RecipeBuilder builder = new RecipeBuilder(exporter, shapedExtremeCrafting);
        for (Object itemInput : recipe.getInput()) {
            if (itemInput == null) {
                builder.skipItemInput();
                continue;
            } else if (itemInput instanceof List && ((List<?>) itemInput).isEmpty()) {
                logger.warn("Shaped ore crafting recipe with empty list ingredient: {}", recipe);
                builder.skipItemInput();
                continue;
            }

            handleItemInput(builder, itemInput);
        }
        builder.addItemOutput(recipe.getRecipeOutput()).build();
    }

    private void processShapelessRecipe(ExtremeShapelessRecipe recipe) {
        // Apparently this actually happens? At least, according to a comment in NEI source.
        if (recipe.recipeItems == null) {
            logger.warn("Crafting recipe with null inputs: {}", recipe);
            return;
        }

        RecipeBuilder builder = new RecipeBuilder(exporter, shapelessExtremeCrafting);
        for (Object itemInput : recipe.recipeItems) {
            handleItemInput(builder, itemInput);
        }
        builder.addItemOutput(recipe.getRecipeOutput()).build();
    }

    private void processShapelessOreRecipe(ShapelessOreRecipe recipe) {
        RecipeBuilder builder = new RecipeBuilder(exporter, shapelessExtremeCrafting);
        for (Object itemInput : recipe.getInput()) {
            if (itemInput instanceof List && ((List<?>) itemInput).isEmpty()) {
                logger.warn("Shapeless ore crafting recipe with empty list ingredient: {}", recipe);
                builder.skipItemInput();
                continue;
            }

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
            logger.warn("Crafting recipe with bad stack size: {}", Arrays.toString(itemStacks));
        }

        builder.addItemGroupInput(fixedItemStacks);
    }
}
