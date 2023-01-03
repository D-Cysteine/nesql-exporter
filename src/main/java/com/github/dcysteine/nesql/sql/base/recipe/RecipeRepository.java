package com.github.dcysteine.nesql.sql.base.recipe;

import com.github.dcysteine.nesql.sql.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends Repository<Recipe, String> {
    @Query(value = "SELECT * FROM RECIPE WHERE ID IN ("
            + "SELECT RECIPESWITHINPUT_ID FROM RECIPE_ITEMGROUP WHERE ITEMINPUTS_ID IN ("
            + "SELECT ITEMGROUP_ID FROM ITEMGROUP_ITEMSTACKS WHERE ITEM_ID = ?1))",
            nativeQuery = true)
    List<Recipe> findByItemInput(String itemId);

    @Query(value = "SELECT * FROM RECIPE WHERE ID IN ("
            + "SELECT RECIPESWITHINPUT_ID FROM RECIPE_ITEMGROUP WHERE ITEMINPUTS_ID IN ("
            + "SELECT ITEMGROUP_ID FROM ITEMGROUP_WILDCARDITEMSTACKS WHERE ITEMID = ?1))",
            nativeQuery = true)
    List<Recipe> findByWildcardItemInput(int minecraftItemId);

    @Query(value = "SELECT * FROM RECIPE WHERE ID IN ("
            + "SELECT RECIPE_ID FROM RECIPE_ITEMOUTPUTS WHERE ITEM_ID = ?1)",
            nativeQuery = true)
    List<Recipe> findByItemOutput(String itemId);
}