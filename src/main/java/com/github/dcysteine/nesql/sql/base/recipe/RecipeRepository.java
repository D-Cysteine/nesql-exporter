package com.github.dcysteine.nesql.sql.base.recipe;

import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, String> {
}
