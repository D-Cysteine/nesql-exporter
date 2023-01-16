package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.recipe.Dimension;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import jakarta.persistence.EntityManager;

public class RecipeTypeFactory extends EntityFactory<RecipeType, String> {
    public RecipeTypeFactory(EntityManager entityManager) {
        super(entityManager);
    }

    public RecipeType getRecipeType(
            String id, String category, String type, Item icon, boolean shapeless,
            Dimension itemInputDimension, Dimension fluidInputDimension,
            Dimension itemOutputDimension, Dimension fluidOutputDimension) {
        RecipeType recipeType =
                new RecipeType(
                        id, category, type, icon, shapeless,
                        itemInputDimension, fluidInputDimension,
                        itemOutputDimension, fluidOutputDimension);
        return findOrPersist(RecipeType.class, recipeType);
    }
}
