package com.github.dcysteine.nesql.exporter.util;

import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.github.dcysteine.nesql.sql.base.item.WildcardItemStack;
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

    public static long countWildcardItemGroups(EntityManager entityManager) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(*) FROM ItemGroup ig WHERE ig.wildcardItemStacks IS NOT EMPTY");
        return (long) query.getSingleResult();
    }

    public static Stream<ItemGroup> getWildcardItemGroups(EntityManager entityManager) {
        TypedQuery<ItemGroup> query = entityManager.createQuery(
                "SELECT ig FROM ItemGroup ig WHERE ig.wildcardItemStacks IS NOT EMPTY",
                ItemGroup.class);
        return query.getResultStream();
    }

    public static Stream<ItemStack> resolveWildcardItemStack(
            EntityManager entityManager, WildcardItemStack wildcardItemStack) {
        StringBuilder queryBuilder = new StringBuilder("SELECT i FROM Item i");
        queryBuilder.append(" WHERE i.itemId = ").append(wildcardItemStack.getItemId());
        if (!wildcardItemStack.isWildcardItemDamage()) {
            queryBuilder.append(" AND i.itemDamage = ").append(wildcardItemStack.getItemDamage());
        }
        if (!wildcardItemStack.isWildcardNbt()) {
            queryBuilder.append(" AND i.nbt = '").append(wildcardItemStack.getNbt()).append("'");
        }

        TypedQuery<Item> query = entityManager.createQuery(queryBuilder.toString(), Item.class);
        Stream<Item> items = query.getResultStream();

        int stackSize = wildcardItemStack.getStackSize();
        return items.map(item -> new ItemStack(item, stackSize));
    }

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
