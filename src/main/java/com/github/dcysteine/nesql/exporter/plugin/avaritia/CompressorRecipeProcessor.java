package com.github.dcysteine.nesql.exporter.plugin.avaritia;

import codechicken.nei.NEIServerUtils;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.RecipeBuilder;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import fox.spiteful.avaritia.crafting.CompressorManager;
import fox.spiteful.avaritia.crafting.CompressorRecipe;
import net.minecraft.item.ItemStack;

import java.util.List;

public class CompressorRecipeProcessor extends PluginHelper {
    private final RecipeType compressor;

    public CompressorRecipeProcessor(
            PluginExporter exporter, AvaritiaRecipeTypeHandler recipeTypeHandler) {
        super(exporter);
        this.compressor =
                recipeTypeHandler.getRecipeType(
                        AvaritiaRecipeTypeHandler.AvaritiaRecipeType.COMPRESSOR);
    }

    public void process() {
        List<CompressorRecipe> recipes = CompressorManager.getRecipes();
        int total = recipes.size();
        logger.info("Processing {} Avaritia compressor recipes...", total);

        int count = 0;
        for (CompressorRecipe recipe : recipes) {
            count++;

            ItemStack[] itemStacks = NEIServerUtils.extractRecipeItems(recipe.getIngredient());
            RecipeBuilder builder = new RecipeBuilder(exporter, compressor);
            if (itemStacks == null || itemStacks.length == 0) {
                builder.skipItemInput();
            } else if (itemStacks.length == 1) {
                builder.addItemInput(itemStacks[0]);
            } else {
                builder.addItemGroupInput(itemStacks);
            }

            builder.addItemOutput(recipe.getOutput()).build();

            if (Logger.intermittentLog(count)) {
                logger.info("Processed Avaritia compressor recipe {} of {}", count, total);
                logger.info("Most recent recipe: {}", recipe.getOutput().getDisplayName());
            }
        }

        exporterState.flushEntityManager();
        logger.info("Finished processing Avaritia compressor recipes!");
    }
}
