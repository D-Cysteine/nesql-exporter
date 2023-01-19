package com.github.dcysteine.nesql.exporter.plugin.base.processor;

import codechicken.nei.NEIServerUtils;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.base.BasePluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.BaseRecipeType;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.RecipeBuilder;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import jakarta.persistence.EntityManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.Map;

public class FurnaceRecipeProcessor {
    private final EntityManager entityManager;
    private final RecipeType furnace;

    public FurnaceRecipeProcessor(BasePluginExporter plugin, EntityManager entityManager) {
        this.entityManager = entityManager;
        this.furnace = plugin.getRecipeType(BaseRecipeType.FURNACE);
    }

    public void process() {
        @SuppressWarnings("unchecked")
        Map<ItemStack, ItemStack> recipes = FurnaceRecipes.smelting().getSmeltingList();
        int total = recipes.size();
        Logger.BASE.info("Processing {} furnace recipes...", total);

        int count = 0;
        for (Map.Entry<ItemStack, ItemStack> recipe : recipes.entrySet()) {
            count++;

            ItemStack[] itemStacks = NEIServerUtils.extractRecipeItems(recipe.getKey());
            RecipeBuilder builder = new RecipeBuilder(entityManager, furnace);
            if (itemStacks == null || itemStacks.length == 0) {
                builder.skipItemInput();
            } else if (itemStacks.length == 1) {
                builder.addItemInput(itemStacks[0], true);
            } else {
                builder.addItemGroupInput(itemStacks, true);
            }

            builder.addItemOutput(recipe.getValue()).build();

            if (Logger.intermittentLog(count)) {
                Logger.BASE.info("Processed furnace recipe {} of {}", count, total);
                Logger.BASE.info("Most recent recipe: {}", recipe.getKey().getDisplayName());
            }
        }

        Logger.BASE.info("Finished processing furnace recipes!");
    }
}
