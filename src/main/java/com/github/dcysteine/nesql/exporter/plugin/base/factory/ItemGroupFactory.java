package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.sql.base.recipe.ItemGroup;
import com.github.dcysteine.nesql.sql.base.recipe.ItemStack;
import jakarta.persistence.EntityManager;

import java.util.SortedSet;

public class ItemGroupFactory extends EntityFactory<ItemGroup, String> {
    public ItemGroupFactory(EntityManager entityManager) {
        super(entityManager);
    }

    public ItemGroup getItemGroup(String id, SortedSet<ItemStack> itemStacks) {
        ItemGroup itemGroup = new ItemGroup(id, itemStacks);
        return findOrPersist(ItemGroup.class, itemGroup);
    }
}
