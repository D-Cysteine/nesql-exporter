package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.util.ItemUtil;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStackWithProbability;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.github.dcysteine.nesql.sql.base.item.ItemStackWithProbability;
import com.github.dcysteine.nesql.sql.base.item.WildcardItemStack;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeInfo;
import jakarta.persistence.EntityManager;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/** Helper class that builds {@link Recipe} instances. */
public class RecipeBuilder {
    private final ItemFactory itemFactory;
    private final FluidFactory fluidFactory;
    private final ItemGroupFactory itemGroupFactory;
    private final FluidGroupFactory fluidGroupFactory;
    private final RecipeFactory recipeFactory;
    private final RecipeInfo recipeInfo;
    private final Map<Integer, ItemGroup> itemInputs;
    private final Map<Integer, FluidGroup> fluidInputs;
    private final Map<Integer, ItemStackWithProbability> itemOutputs;
    private final Map<Integer, FluidStackWithProbability> fluidOutputs;
    private int itemInputsIndex;
    private int fluidInputsIndex;
    private int itemOutputsIndex;
    private int fluidOutputsIndex;

    public RecipeBuilder(EntityManager entityManager, RecipeInfo recipeInfo) {
        this.itemFactory = new ItemFactory(entityManager);
        this.fluidFactory = new FluidFactory(entityManager);
        this.itemGroupFactory = new ItemGroupFactory(entityManager);
        this.fluidGroupFactory = new FluidGroupFactory(entityManager);
        this.recipeFactory = new RecipeFactory(entityManager);
        this.recipeInfo = recipeInfo;
        this.itemInputs = new HashMap<>();
        this.fluidInputs = new HashMap<>();
        this.itemOutputs = new HashMap<>();
        this.fluidOutputs = new HashMap<>();
        this.itemInputsIndex = 0;
        this.fluidInputsIndex = 0;
        this.itemOutputsIndex = 0;
        this.fluidOutputsIndex = 0;
    }

    public RecipeBuilder addItemInput(net.minecraft.item.ItemStack input, boolean handleWildcard) {
        if (handleWildcard && ItemUtil.isWildcardItem(input)) {
            itemInputs.put(
                    itemInputsIndex++,
                    itemGroupFactory.getItemGroup(buildWildcardItemStack(input)));
        } else {
            itemInputs.put(
                    itemInputsIndex++,
                    itemGroupFactory.getItemGroup(buildItemStack(input)));
        }

        return this;
    }

    /** Adds each item in {@code inputs} into a separate input slot. */
    public RecipeBuilder addAllItemInput(
            Iterable<net.minecraft.item.ItemStack> inputs, boolean handleWildcard) {
        inputs.forEach(input -> addItemInput(input, handleWildcard));
        return this;
    }

    /** Adds each item in {@code inputs} into a separate input slot. */
    public RecipeBuilder addAllItemInput(
            net.minecraft.item.ItemStack[] inputs, boolean handleWildcard) {
        Stream.of(inputs).forEach(input -> addItemInput(input, handleWildcard));
        return this;
    }

    /** Adds all items in {@code inputs} into a single input slot, as an item group. */
    public RecipeBuilder addItemGroupInput(
            Collection<net.minecraft.item.ItemStack> inputs, boolean handleWildcard) {
        SortedSet<ItemStack> itemStacks = new TreeSet<>();
        SortedSet<WildcardItemStack> wildcardItemStacks = new TreeSet<>();
        for (net.minecraft.item.ItemStack input : inputs) {
            if (handleWildcard && ItemUtil.isWildcardItem(input)) {
                wildcardItemStacks.add(buildWildcardItemStack(input));
            } else {
                itemStacks.add(buildItemStack(input));
            }
        }

        itemInputs.put(
                itemInputsIndex++, itemGroupFactory.getItemGroup(itemStacks, wildcardItemStacks));
        return this;
    }

    /** Adds all items in {@code inputs} into a single input slot, as an item group. */
    public RecipeBuilder addItemGroupInput(
            net.minecraft.item.ItemStack[] inputs, boolean handleWildcard) {
        return addItemGroupInput(Arrays.asList(inputs), handleWildcard);
    }

    public RecipeBuilder skipItemInput() {
        if (recipeInfo.isShapeless()) {
            Logger.BASE.warn("Skipping item input index for shapeless recipe!");
        }

        itemInputsIndex++;
        return this;
    }

    public RecipeBuilder addFluidInput(net.minecraftforge.fluids.FluidStack input) {
        fluidInputs.put(
                fluidInputsIndex++, fluidGroupFactory.getFluidGroup(buildFluidStack(input)));
        return this;
    }

    /** Adds each item in {@code inputs} into a separate input slot. */
    public RecipeBuilder addAllFluidInput(Iterable<net.minecraftforge.fluids.FluidStack> inputs) {
        inputs.forEach(this::addFluidInput);
        return this;
    }

    /** Adds each item in {@code inputs} into a separate input slot. */
    public RecipeBuilder addAllFluidInput(net.minecraftforge.fluids.FluidStack[] inputs) {
        Stream.of(inputs).forEach(this::addFluidInput);
        return this;
    }

    /** Adds all fluids in {@code inputs} into a single input slot, as an fluid group. */
    public RecipeBuilder addFluidGroupInput(Iterable<net.minecraftforge.fluids.FluidStack> inputs) {
        SortedSet<FluidStack> fluidStacks =
                StreamSupport.stream(inputs.spliterator(), false)
                        .filter(Objects::nonNull)
                        .map(this::buildFluidStack)
                        .collect(Collectors.toCollection(TreeSet::new));
        fluidInputs.put(fluidInputsIndex++, fluidGroupFactory.getFluidGroup(fluidStacks));
        return this;
    }

    /** Adds all fluids in {@code inputs} into a single input slot, as an fluid group. */
    public RecipeBuilder addFluidGroupInput(net.minecraftforge.fluids.FluidStack[] inputs) {
        return addFluidGroupInput(Arrays.asList(inputs));
    }

    public RecipeBuilder skipFluidInput() {
        if (recipeInfo.isShapeless()) {
            Logger.BASE.warn("Skipping fluid input index for shapeless recipe!");
        }

        fluidInputsIndex++;
        return this;
    }

    public RecipeBuilder addItemOutput(net.minecraft.item.ItemStack output, double probability) {
        if (output == null) {
            return skipItemOutput();
        }

        itemOutputs.put(itemOutputsIndex++, buildItemStackWithProbability(output, probability));
        return this;
    }

    public RecipeBuilder addItemOutput(net.minecraft.item.ItemStack output) {
        return addItemOutput(output, 1.0d);
    }

    public RecipeBuilder addAllItemOutput(Iterable<net.minecraft.item.ItemStack> outputs) {
        outputs.forEach(this::addItemOutput);
        return this;
    }

    public RecipeBuilder addAllItemOutput(net.minecraft.item.ItemStack[] outputs) {
        Stream.of(outputs).forEach(this::addItemOutput);
        return this;
    }

    public RecipeBuilder skipItemOutput() {
        if (recipeInfo.isShapeless()) {
            Logger.BASE.warn("Skipping item output index for shapeless recipe!");
        }

        itemOutputsIndex++;
        return this;
    }

    public RecipeBuilder addFluidOutput(
            net.minecraftforge.fluids.FluidStack output, double probability) {
        fluidOutputs.put(fluidOutputsIndex++, buildFluidStackWithProbability(output, probability));
        return this;
    }

    public RecipeBuilder addFluidOutput(net.minecraftforge.fluids.FluidStack output) {
        return addFluidOutput(output, 1.0d);
    }

    public RecipeBuilder addAllFluidOutput(Iterable<net.minecraftforge.fluids.FluidStack> outputs) {
        outputs.forEach(this::addFluidOutput);
        return this;
    }

    public RecipeBuilder addAllFluidOutput(net.minecraftforge.fluids.FluidStack[] outputs) {
        Stream.of(outputs).forEach(this::addFluidOutput);
        return this;
    }

    public RecipeBuilder skipFluidOutput() {
        if (recipeInfo.isShapeless()) {
            Logger.BASE.warn("Skipping fluid output index for shapeless recipe!");
        }

        fluidOutputsIndex++;
        return this;
    }

    public Recipe build() {
        return recipeFactory.getRecipe(
                recipeInfo, itemInputs, fluidInputs, itemOutputs, fluidOutputs);
    }

    private ItemStack buildItemStack(net.minecraft.item.ItemStack itemStack) {
        return new ItemStack(itemFactory.getItem(itemStack), itemStack.stackSize);
    }

    private ItemStackWithProbability buildItemStackWithProbability(
            net.minecraft.item.ItemStack itemStack, double probability) {
        return new ItemStackWithProbability(
                itemFactory.getItem(itemStack), itemStack.stackSize, probability);
    }

    private WildcardItemStack buildWildcardItemStack(net.minecraft.item.ItemStack itemStack) {
        return new WildcardItemStack(ItemUtil.getItemId(itemStack), itemStack.stackSize);
    }

    private FluidStack buildFluidStack(net.minecraftforge.fluids.FluidStack fluidStack) {
        return new FluidStack(fluidFactory.getFluid(fluidStack), fluidStack.amount);
    }

    private FluidStackWithProbability buildFluidStackWithProbability(
            net.minecraftforge.fluids.FluidStack fluidStack, double probability) {
        return new FluidStackWithProbability(
                fluidFactory.getFluid(fluidStack), fluidStack.amount, probability);
    }
}