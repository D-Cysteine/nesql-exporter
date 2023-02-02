package com.github.dcysteine.nesql.exporter.plugin.base.processor;

import codechicken.nei.NEIServerUtils;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.BaseRecipeTypeHandler;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.RecipeBuilder;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.Map;

public class FurnaceRecipeProcessor extends PluginHelper {
    private final RecipeType furnace;

    public FurnaceRecipeProcessor(
            PluginExporter exporter, BaseRecipeTypeHandler recipeTypeHandler) {
        super(exporter);
        this.furnace =
                recipeTypeHandler.getRecipeType(BaseRecipeTypeHandler.BaseRecipeType.FURNACE);
    }

    public void process() {
        @SuppressWarnings("unchecked")
        Map<ItemStack, ItemStack> recipes = FurnaceRecipes.smelting().getSmeltingList();
        int total = recipes.size();
        logger.info("Processing {} furnace recipes...", total);

        int count = 0;
        for (Map.Entry<ItemStack, ItemStack> recipe : recipes.entrySet()) {
            count++;

            ItemStack[] itemStacks = NEIServerUtils.extractRecipeItems(recipe.getKey());
            RecipeBuilder builder = new RecipeBuilder(exporter, furnace);
            if (itemStacks == null || itemStacks.length == 0) {
                builder.skipItemInput();
            } else if (itemStacks.length == 1) {
                builder.addItemInput(itemStacks[0], true);
            } else {
                builder.addItemGroupInput(itemStacks, true);
            }

            builder.addItemOutput(recipe.getValue()).build();

            if (Logger.intermittentLog(count)) {
                logger.info("Processed furnace recipe {} of {}", count, total);
                logger.info("Most recent recipe: {}", recipe.getKey().getDisplayName());
            }
        }

        logger.info("Finished processing furnace recipes!");
    }
}
