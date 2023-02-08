package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import codechicken.nei.ItemList;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.proto.ItemGroupPb;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.ItemUtil;
import com.github.dcysteine.nesql.exporter.util.ProtoBuilder;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ItemGroupFactory extends EntityFactory<ItemGroup, String> {
    private final ItemFactory itemFactory;

    public ItemGroupFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
    }

    public ItemGroup get(net.minecraft.item.ItemStack itemStack) {
        return get(ImmutableList.of(itemStack));
    }

    public ItemGroup get(Collection<net.minecraft.item.ItemStack> itemStacks) {
        return get(itemStacks, Optional.empty());
    }

    /**
     * {@code itemStack.stackSize} will be overridden with {@code overrideStackSize}.
     * This is needed by things like BetterQuesting's {@code BigItemStack},
     * which has stack size larger than is supported by Minecraft's {@code ItemStack}.
     */
    public ItemGroup get(
            Collection<net.minecraft.item.ItemStack> itemStack, int overrideStackSize) {
        return get(itemStack, Optional.of(overrideStackSize));
    }

    /**
     * Pass in a non-empty optional for {@code overrideStackSize} to override all stack sizes to the
     * given value.
     *
     * <p>Overriding the stack size is needed to support things like BetterQuesting's
     * {@code BigItemStack}, which has stack size larger than is supported by Minecraft's
     * {@code ItemStack}.
     */
    public ItemGroup get(
            Collection<net.minecraft.item.ItemStack> itemStacks,
            Optional<Integer> overrideStackSize) {
        Set<ItemStack> itemStackEntities = new HashSet<>();
        for (net.minecraft.item.ItemStack itemStack : itemStacks) {
            if (itemStack == null) {
                continue;
            }

            int stackSize = overrideStackSize.orElse(itemStack.stackSize);
            if (ItemUtil.hasWildcardItemDamage(itemStack)) {
                List<net.minecraft.item.ItemStack> permutations =
                        ItemList.itemMap.get(itemStack.getItem());
                if (!permutations.isEmpty()) {
                    permutations.stream()
                            .map(itemFactory::get)
                            .map(item -> new ItemStack(item, stackSize))
                            .forEach(itemStackEntities::add);
                } else {
                    net.minecraft.item.ItemStack baseItemStack = itemStack.copy();
                    baseItemStack.setItemDamage(0);
                    itemStackEntities.add(new ItemStack(itemFactory.get(baseItemStack), stackSize));
                }
            } else {
                itemStackEntities.add(new ItemStack(itemFactory.get(itemStack), stackSize));
            }
        }

        return get(itemStackEntities);
    }

    public ItemGroup get(Set<ItemStack> itemStacks) {
        ItemGroupPb itemGroupPb = ProtoBuilder.buildItemGroupPb(itemStacks);
        String id = IdPrefixUtil.ITEM_GROUP.applyPrefix(StringUtil.encodeProto(itemGroupPb));
        ItemGroup itemGroup = new ItemGroup(id, itemStacks);
        return findOrPersist(ItemGroup.class, itemGroup);
    }
}
