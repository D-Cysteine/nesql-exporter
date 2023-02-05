package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.proto.ItemGroupPb;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.ItemUtil;
import com.github.dcysteine.nesql.exporter.util.ProtoBuilder;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.github.dcysteine.nesql.sql.base.item.WildcardItemStack;
import com.google.common.collect.ImmutableList;
import cpw.mods.fml.common.registry.GameRegistry;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ItemGroupFactory extends EntityFactory<ItemGroup, String> {
    private final ItemFactory itemFactory;

    public ItemGroupFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
    }

    public ItemGroup get(net.minecraft.item.ItemStack itemStack, boolean handleWildcard) {
        return get(ImmutableList.of(itemStack), handleWildcard);
    }

    public ItemGroup get(
            Collection<net.minecraft.item.ItemStack> itemStacks, boolean handleWildcard) {
        return get(itemStacks, Optional.empty(), handleWildcard);
    }

    /**
     * {@code itemStack.stackSize} will be overridden with {@code overrideStackSize}. This is needed
     * by things like BetterQuesting's {@code BigItemStack}, which has stack size larger than is
     * supported by Minecraft's {@code ItemStack}.
     */
    public ItemGroup get(
            Collection<net.minecraft.item.ItemStack> itemStack,
            int overrideStackSize, boolean handleWildcard) {
        return get(itemStack, Optional.of(overrideStackSize), handleWildcard);
    }

    /**
     * Pass in a non-empty optional for {@code overrideStackSize} to override all stack sizes to the
     * given value.
     *
     * Overriding the stack size is needed to support things like BetterQuesting's
     * {@code BigItemStack}, which has stack size larger than is supported by Minecraft's
     * {@code ItemStack}.
     */
    public ItemGroup get(
            Collection<net.minecraft.item.ItemStack> itemStacks,
            Optional<Integer> overrideStackSize, boolean handleWildcard) {
        Set<ItemStack> directItemStacks = new HashSet<>();
        Set<WildcardItemStack> wildcardItemStacks = new HashSet<>();
        for (net.minecraft.item.ItemStack itemStack : itemStacks) {
            if (itemStack == null) {
                continue;
            }

            int stackSize = overrideStackSize.orElse(itemStack.stackSize);
            if (handleWildcard) {
                boolean wildcardItemDamage = ItemUtil.hasWildcardItemDamage(itemStack);
                boolean wildcardNbt = ItemUtil.hasWildcardNbt(itemStack);

                if (wildcardItemDamage || wildcardNbt) {
                    GameRegistry.UniqueIdentifier uniqueId =
                            GameRegistry.findUniqueIdentifierFor(itemStack.getItem());
                    int itemDamage = wildcardItemDamage ? 0 : itemStack.getItemDamage();
                    String nbt = wildcardNbt || !itemStack.hasTagCompound()
                            ? "" : itemStack.getTagCompound().toString();

                    wildcardItemStacks.add(
                            new WildcardItemStack(
                                    uniqueId.modId, uniqueId.name, ItemUtil.getItemId(itemStack),
                                    wildcardItemDamage, itemDamage, wildcardNbt, nbt, stackSize));
                    continue;
                }
            }

            directItemStacks.add(new ItemStack(itemFactory.get(itemStack), stackSize));
        }

        ItemGroupPb itemGroupPb =
                ProtoBuilder.buildItemGroupPb(directItemStacks, wildcardItemStacks);
        String id = IdPrefixUtil.ITEM_GROUP.applyPrefix(StringUtil.encodeProto(itemGroupPb));
        ItemGroup itemGroup = new ItemGroup(id, directItemStacks, wildcardItemStacks);
        return findOrPersist(ItemGroup.class, itemGroup);
    }
}
