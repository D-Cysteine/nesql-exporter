package com.github.dcysteine.nesql.exporter.util;

import com.github.dcysteine.nesql.exporter.proto.FluidGroupPb;
import com.github.dcysteine.nesql.exporter.proto.FluidPb;
import com.github.dcysteine.nesql.exporter.proto.ItemGroupPb;
import com.github.dcysteine.nesql.exporter.proto.ItemPb;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.github.dcysteine.nesql.sql.base.item.WildcardItem;

import javax.annotation.Nullable;
import java.util.SortedSet;

/** Contains shared methods for building protos. */
public final class ProtoBuilder {
    // Static class.
    private ProtoBuilder() {}

    public static ItemGroupPb buildItemGroupPb(
            SortedSet<ItemStack> itemStacks, SortedSet<WildcardItem> wildcardItems) {
        ItemGroupPb.Builder builder = ItemGroupPb.newBuilder();
        itemStacks.forEach(itemStack -> builder.addItem(buildItemPb(itemStack)));
        wildcardItems.forEach(wildcardItem -> builder.addWildcardItemId(wildcardItem.getId()));
        return builder.build();
    }

    public static ItemGroupPb buildItemGroupPb(ItemGroup itemGroup) {
        ItemGroupPb.Builder builder = ItemGroupPb.newBuilder();
        itemGroup.getItemStacks().stream()
                .map(ProtoBuilder::buildItemPb)
                .forEach(builder::addItem);
        itemGroup.getWildcardItems().stream()
                .map(WildcardItem::getId)
                .forEach(builder::addWildcardItemId);
        return builder.build();
    }

    public static FluidGroupPb buildFluidGroupPb(SortedSet<FluidStack> fluidStacks) {
        FluidGroupPb.Builder builder = FluidGroupPb.newBuilder();
        fluidStacks.forEach(fluidStack -> builder.addFluid(buildFluidPb(fluidStack)));
        return builder.build();
    }

    public static FluidGroupPb buildFluidGroupPb(FluidGroup fluidGroup) {
        FluidGroupPb.Builder builder = FluidGroupPb.newBuilder();
        fluidGroup.getFluidStacks().stream()
                .map(ProtoBuilder::buildFluidPb)
                .forEach(builder::addFluid);
        return builder.build();
    }

    public static ItemPb buildItemPb(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            return ItemPb.getDefaultInstance();
        }

        Item item = itemStack.getItem();
        ItemPb.Builder builder = ItemPb.newBuilder()
                .setItemId(item.getItemId())
                .setDamage(item.getItemDamage())
                .setStackSize(itemStack.getStackSize());
        if (item.hasNbt()) {
            builder.setNbt(item.getNbt());
        }
        return builder.build();
    }

    public static FluidPb buildFluidPb(@Nullable FluidStack fluidStack) {
        if (fluidStack == null) {
            return FluidPb.getDefaultInstance();
        }

        Fluid fluid = fluidStack.getFluid();
        FluidPb.Builder builder = FluidPb.newBuilder()
                .setFluidId(fluid.getFluidId())
                .setAmount(fluidStack.getAmount());
        if (fluid.hasNbt()) {
            builder.setNbt(fluid.getNbt());
        }
        return builder.build();
    }
}
