package com.github.dcysteine.nesql.exporter.plugin.base.processor;

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
        recipes.forEach((key, value) -> {
            new RecipeBuilder(entityManager, RecipeType.MINECRAFT_FURNACE)
                    .addItemInput(key)
                    .addItemOutput(value)
                    .build();
        });
    }
}
