package com.github.dcysteine.nesql.exporter.util;

import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.stream.Stream;

/**
 * Utility class containing methods for querying the database.
 *
 * <p>If the database schema is changed, this class's queries may need to be updated.
 */
public final class QueryUtil {
    // Static class.
    private QueryUtil() {}

    public static long countRecipes(EntityManager entityManager) {
        Query query = entityManager.createQuery("SELECT COUNT(*) FROM Recipe r");
        return (long) query.getSingleResult();
    }

    public static Stream<Recipe> getRecipes(EntityManager entityManager) {
        TypedQuery<Recipe> query =
                entityManager.createQuery("SELECT r FROM Recipe r", Recipe.class);
        return query.getResultStream();
    }
}
