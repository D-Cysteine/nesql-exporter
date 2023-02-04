package com.github.dcysteine.nesql.exporter.util;

import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.github.dcysteine.nesql.sql.base.item.WildcardItemStack;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class containing methods for querying the database.
 *
 * <p>If the database schema is changed, this class's queries may need to be updated.
 */
public final class QueryUtil {
    // Static class.
    private QueryUtil() {}

    public static List<ItemGroup> getWildcardItemGroups(EntityManager entityManager) {
        TypedQuery<ItemGroup> query = entityManager.createQuery(
                "SELECT ig FROM ItemGroup ig WHERE ig.wildcardItemStacks IS NOT EMPTY",
                ItemGroup.class);
        return query.getResultList();
    }

    public static List<ItemStack> resolveWildcardItemStack(
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
        List<Item> items = query.getResultList();

        int stackSize = wildcardItemStack.getStackSize();
        return items.stream()
                .map(item -> new ItemStack(item, stackSize))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Recipe> getRecipes(EntityManager entityManager) {
        TypedQuery<Recipe> query =
                entityManager.createQuery("SELECT r FROM Recipe r", Recipe.class);
        return query.getResultList();
    }
}
