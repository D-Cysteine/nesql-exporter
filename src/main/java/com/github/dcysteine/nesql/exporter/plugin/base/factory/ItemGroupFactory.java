package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.plugin.Database;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.proto.ItemGroupPb;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.ProtoBuilder;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.github.dcysteine.nesql.sql.base.item.WildcardItemStack;
import com.google.common.collect.ImmutableSortedSet;

import java.util.SortedSet;

public class ItemGroupFactory extends EntityFactory<ItemGroup, String> {
    public ItemGroupFactory(Database database) {
        super(database);
    }

    public ItemGroup getItemGroup(
            SortedSet<ItemStack> itemStacks, SortedSet<WildcardItemStack> wildcardItemStacks) {
        ItemGroupPb itemGroupPb = ProtoBuilder.buildItemGroupPb(itemStacks, wildcardItemStacks);
        String id = IdPrefixUtil.ITEM_GROUP.applyPrefix(StringUtil.encodeProto(itemGroupPb));
        ItemGroup itemGroup = new ItemGroup(id, itemStacks, wildcardItemStacks);
        return findOrPersist(ItemGroup.class, itemGroup);
    }

    public ItemGroup getItemGroup(ItemStack itemStack) {
        return getItemGroup(ImmutableSortedSet.of(itemStack), ImmutableSortedSet.of());
    }

    public ItemGroup getItemGroup(WildcardItemStack wildcardItemStack) {
        return getItemGroup(ImmutableSortedSet.of(), ImmutableSortedSet.of(wildcardItemStack));
    }
}
