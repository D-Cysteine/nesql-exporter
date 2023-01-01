package com.github.dcysteine.nesql.exporter.plugin.base.processor;

import codechicken.nei.NEIServerUtils;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.RecipeBuilder;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import jakarta.persistence.EntityManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.Map;

public class FurnaceRecipeProcessor {
    private final EntityManager entityManager;

    public FurnaceRecipeProcessor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public void process() {
        Map<ItemStack, ItemStack> recipes = FurnaceRecipes.smelting().getSmeltingList();
        int total = recipes.size();
        Logger.MOD.info("Processing {} furnace recipes...", total);

        int count = 0;
        for (Map.Entry<ItemStack, ItemStack> recipe : recipes.entrySet()) {
            count++;

            ItemStack[] itemStacks = NEIServerUtils.extractRecipeItems(recipe.getKey());
            RecipeBuilder builder = new RecipeBuilder(entityManager, RecipeType.MINECRAFT_FURNACE);
            if (itemStacks == null || itemStacks.length == 0) {
                builder.skipItemInput();
            } else if (itemStacks.length == 1) {
                builder.addItemInput(itemStacks[0], true);
            } else {
                builder.addItemGroupInput(itemStacks, true);
            }

            builder.addItemOutput(recipe.getValue()).build();

            if (Logger.intermittentLog(count)) {
                Logger.MOD.info("Processed furnace recipe {} of {}", count, total);
                Logger.MOD.info("Most recent recipe: {}", recipe.getKey().getDisplayName());
            }
        }

        Logger.MOD.info("Finished processing furnace recipes!");
    }
}
