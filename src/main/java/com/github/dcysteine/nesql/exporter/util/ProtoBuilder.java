package com.github.dcysteine.nesql.exporter.util;

import com.github.dcysteine.nesql.exporter.proto.FluidGroupPb;
import com.github.dcysteine.nesql.exporter.proto.FluidStackPb;
import com.github.dcysteine.nesql.exporter.proto.FluidStackWithProbabilityPb;
import com.github.dcysteine.nesql.exporter.proto.ItemGroupPb;
import com.github.dcysteine.nesql.exporter.proto.ItemStackPb;
import com.github.dcysteine.nesql.exporter.proto.ItemStackWithProbabilityPb;
import com.github.dcysteine.nesql.exporter.proto.RecipeTypePb;
import com.github.dcysteine.nesql.exporter.proto.RecipePb;
import com.github.dcysteine.nesql.exporter.proto.WildcardItemStackPb;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStackWithProbability;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.github.dcysteine.nesql.sql.base.item.ItemStackWithProbability;
import com.github.dcysteine.nesql.sql.base.item.WildcardItemStack;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;

import java.util.Map;
import java.util.SortedSet;

/** Contains shared methods for building protos. */
public final class ProtoBuilder {
    // Static class.
    private ProtoBuilder() {}

    public static RecipePb buildRecipePb(
            RecipeType recipeType,
            Map<Integer, ItemGroup> itemInputs,
            Map<Integer, FluidGroup> fluidInputs,
            Map<Integer, ItemStackWithProbability> itemOutputs,
            Map<Integer, FluidStackWithProbability> fluidOutputs) {
        RecipePb.Builder builder =
                RecipePb.newBuilder().setRecipeType(buildRecipeTypePb(recipeType));
        itemInputs.forEach((key, value) -> builder.putItemInput(key, buildItemGroupPb(value)));
        fluidInputs.forEach((key, value) -> builder.putFluidInput(key, buildFluidGroupPb(value)));
        itemOutputs.forEach(
                (key, value) -> builder.putItemOutput(key, buildItemStackWithProbabilityPb(value)));
        fluidOutputs.forEach(
                (key, value) ->
                        builder.putFluidOutput(key, buildFluidStackWithProbabilityPb(value)));
        return builder.build();
    }

    public static RecipeTypePb buildRecipeTypePb(RecipeType recipeType) {
        return RecipeTypePb.newBuilder()
                .setCategory(recipeType.getCategory())
                .setType(recipeType.getType())
                .build();
    }

    public static ItemGroupPb buildItemGroupPb(
            SortedSet<ItemStack> itemStacks, SortedSet<WildcardItemStack> wildcardItemStacks) {
        ItemGroupPb.Builder builder = ItemGroupPb.newBuilder();
        itemStacks.forEach(itemStack -> builder.addItemStack(buildItemStackPb(itemStack)));
        wildcardItemStacks.forEach(
                wildcardItemStack ->
                        builder.addWildcardItemStack(
                                buildWildcardItemStackPb(wildcardItemStack)));
        return builder.build();
    }

    public static ItemGroupPb buildItemGroupPb(ItemGroup itemGroup) {
        ItemGroupPb.Builder builder = ItemGroupPb.newBuilder();
        itemGroup.getItemStacks().stream()
                .map(ProtoBuilder::buildItemStackPb)
                .forEach(builder::addItemStack);
        itemGroup.getWildcardItemStacks().stream()
                .map(ProtoBuilder::buildWildcardItemStackPb)
                .forEach(builder::addWildcardItemStack);
        return builder.build();
    }

    public static FluidGroupPb buildFluidGroupPb(SortedSet<FluidStack> fluidStacks) {
        FluidGroupPb.Builder builder = FluidGroupPb.newBuilder();
        fluidStacks.forEach(fluidStack -> builder.addFluidStack(buildFluidStackPb(fluidStack)));
        return builder.build();
    }

    public static FluidGroupPb buildFluidGroupPb(FluidGroup fluidGroup) {
        FluidGroupPb.Builder builder = FluidGroupPb.newBuilder();
        fluidGroup.getFluidStacks().stream()
                .map(ProtoBuilder::buildFluidStackPb)
                .forEach(builder::addFluidStack);
        return builder.build();
    }

    public static ItemStackPb buildItemStackPb(ItemStack itemStack) {
        Item item = itemStack.getItem();
        ItemStackPb.Builder builder = ItemStackPb.newBuilder()
                .setModId(item.getModId())
                .setInternalName(item.getInternalName())
                .setDamage(item.getItemDamage())
                .setStackSize(itemStack.getStackSize());
        if (item.hasNbt()) {
            builder.setNbt(item.getNbt());
        }
        return builder.build();
    }

    public static ItemStackWithProbabilityPb buildItemStackWithProbabilityPb(
            ItemStackWithProbability itemStack) {
        Item item = itemStack.getItem();
        ItemStackWithProbabilityPb.Builder builder = ItemStackWithProbabilityPb.newBuilder()
                .setModId(item.getModId())
                .setInternalName(item.getInternalName())
                .setDamage(item.getItemDamage())
                .setStackSize(itemStack.getStackSize())
                .setProbability(itemStack.getProbability());
        if (item.hasNbt()) {
            builder.setNbt(item.getNbt());
        }
        return builder.build();
    }

    public static WildcardItemStackPb buildWildcardItemStackPb(
            WildcardItemStack wildcardItemStack) {
        return WildcardItemStackPb.newBuilder()
                .setModId(wildcardItemStack.getModId())
                .setInternalName(wildcardItemStack.getInternalName())
                .setStackSize(wildcardItemStack.getStackSize())
                .build();
    }

    public static FluidStackPb buildFluidStackPb(FluidStack fluidStack) {
        Fluid fluid = fluidStack.getFluid();
        FluidStackPb.Builder builder = FluidStackPb.newBuilder()
                .setModId(fluid.getModId())
                .setInternalName(fluid.getInternalName())
                .setAmount(fluidStack.getAmount());
        if (fluid.hasNbt()) {
            builder.setNbt(fluid.getNbt());
        }
        return builder.build();
    }

    public static FluidStackWithProbabilityPb buildFluidStackWithProbabilityPb(
            FluidStackWithProbability fluidStack) {
        Fluid fluid = fluidStack.getFluid();
        FluidStackWithProbabilityPb.Builder builder = FluidStackWithProbabilityPb.newBuilder()
                .setModId(fluid.getModId())
                .setInternalName(fluid.getInternalName())
                .setAmount(fluidStack.getAmount())
                .setProbability(fluidStack.getProbability());
        if (fluid.hasNbt()) {
            builder.setNbt(fluid.getNbt());
        }
        return builder.build();
    }
}
