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
        Set<ItemStack> directItemStacks = new HashSet<>();
        Set<WildcardItemStack> wildcardItemStacks = new HashSet<>();
        for (net.minecraft.item.ItemStack itemStack : itemStacks) {
            if (itemStack == null) {
                continue;
            }

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
                                    wildcardItemDamage, itemDamage, wildcardNbt, nbt,
                                    itemStack.stackSize));
                    continue;
                }
            }

            directItemStacks.add(
                    new ItemStack(itemFactory.get(itemStack), itemStack.stackSize));
        }

        ItemGroupPb itemGroupPb =
                ProtoBuilder.buildItemGroupPb(directItemStacks, wildcardItemStacks);
        String id = IdPrefixUtil.ITEM_GROUP.applyPrefix(StringUtil.encodeProto(itemGroupPb));
        ItemGroup itemGroup = new ItemGroup(id, directItemStacks, wildcardItemStacks);
        return findOrPersist(ItemGroup.class, itemGroup);
    }
}
