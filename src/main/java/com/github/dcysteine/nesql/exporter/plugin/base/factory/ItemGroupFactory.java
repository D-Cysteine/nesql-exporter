package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.proto.ItemGroupPb;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.exporter.util.ProtoBuilder;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.github.dcysteine.nesql.sql.base.item.WildcardItem;
import com.google.common.collect.ImmutableSortedSet;
import jakarta.persistence.EntityManager;

import java.util.SortedSet;

public class ItemGroupFactory extends EntityFactory<ItemGroup, String> {
    public ItemGroupFactory(EntityManager entityManager) {
        super(entityManager);
    }

    public ItemGroup getItemGroup(
            SortedSet<ItemStack> itemStacks, SortedSet<WildcardItem> wildcardItems) {
        ItemGroupPb itemGroupPb = ProtoBuilder.buildItemGroupPb(itemStacks, wildcardItems);
        ItemGroup itemGroup =
                new ItemGroup(IdUtil.compressProto(itemGroupPb), itemStacks, wildcardItems);
        return findOrPersist(ItemGroup.class, itemGroup);
    }

    public ItemGroup getItemGroup(ItemStack itemStack) {
        return getItemGroup(ImmutableSortedSet.of(itemStack), ImmutableSortedSet.of());
    }

    public ItemGroup getItemGroup(WildcardItem wildcardItem) {
        return getItemGroup(ImmutableSortedSet.of(), ImmutableSortedSet.of(wildcardItem));
    }
}