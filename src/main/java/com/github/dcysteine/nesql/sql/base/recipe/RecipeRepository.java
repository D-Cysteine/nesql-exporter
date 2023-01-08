package com.github.dcysteine.nesql.sql.base.recipe;

import com.github.dcysteine.nesql.sql.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends Repository<Recipe, String> {
    @Query(value = "SELECT * FROM RECIPE WHERE ID IN ("
            + "SELECT RECIPES_WITH_INPUT_ID FROM RECIPE_ITEM_GROUP WHERE ITEM_INPUTS_ID IN ("
            + "SELECT ITEM_GROUP_ID FROM ITEM_GROUP_ITEM_STACKS WHERE ITEM_STACKS_ITEM_ID = ?1))",
            nativeQuery = true)
    List<Recipe> findByItemInput(String itemId);

    @Query(value = "SELECT * FROM RECIPE WHERE ID IN ("
            + "SELECT RECIPES_WITH_INPUT_ID FROM RECIPE_ITEM_GROUP WHERE ITEM_INPUTS_ID IN ("
            + "SELECT ITEM_GROUP_ID FROM ITEM_GROUP_WILDCARD_ITEM_STACKS"
            + " WHERE WILDCARD_ITEM_STACKS_ITEM_ID = ?1))",
            nativeQuery = true)
    List<Recipe> findByWildcardItemInput(int minecraftItemId);

    @Query(value = "SELECT * FROM RECIPE WHERE ID IN ("
            + "SELECT RECIPE_ID FROM RECIPE_ITEM_OUTPUTS WHERE ITEM_OUTPUTS_VALUE_ITEM_ID = ?1)",
            nativeQuery = true)
    List<Recipe> findByItemOutput(String itemId);

    @Query(value = "SELECT * FROM RECIPE WHERE ID IN ("
            + "SELECT RECIPES_WITH_INPUT_ID FROM RECIPE_FLUID_GROUP WHERE FLUID_INPUTS_ID IN ("
            + "SELECT FLUID_GROUP_ID FROM FLUID_GROUP_FLUID_STACKS"
            + " WHERE FLUID_STACKS_FLUID_ID = ?1))",
            nativeQuery = true)
    List<Recipe> findByFluidInput(String fluidId);

    @Query(value = "SELECT * FROM RECIPE WHERE ID IN ("
            + "SELECT RECIPE_ID FROM RECIPE_FLUID_OUTPUTS WHERE FLUID_OUTPUTS_VALUE_FLUID_ID = ?1)",
            nativeQuery = true)
    List<Recipe> findByFluidOutput(String fluidId);
}