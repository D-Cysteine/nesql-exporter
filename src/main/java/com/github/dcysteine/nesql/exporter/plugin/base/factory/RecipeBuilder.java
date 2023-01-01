package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.util.ItemUtil;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.github.dcysteine.nesql.sql.base.item.WildcardItem;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
    private final WildcardItemFactory wildcardItemFactory;
    private final ItemGroupFactory itemGroupFactory;
    private final FluidGroupFactory fluidGroupFactory;
    private final RecipeFactory recipeFactory;
    private final RecipeType recipeType;
    private final List<ItemGroup> itemInputs;
    private final List<FluidGroup> fluidInputs;
    private final List<ItemStack> itemOutputs;
    private final List<FluidStack> fluidOutputs;

    public RecipeBuilder(EntityManager entityManager, RecipeType recipeType) {
        this.itemFactory = new ItemFactory(entityManager);
        this.fluidFactory = new FluidFactory(entityManager);
        this.wildcardItemFactory = new WildcardItemFactory(entityManager);
        this.itemGroupFactory = new ItemGroupFactory(entityManager);
        this.fluidGroupFactory = new FluidGroupFactory(entityManager);
        this.recipeFactory = new RecipeFactory(entityManager);
        this.recipeType = recipeType;
        this.itemInputs = new ArrayList<>();
        this.fluidInputs = new ArrayList<>();
        this.itemOutputs = new ArrayList<>();
        this.fluidOutputs = new ArrayList<>();
    }

    public RecipeBuilder addItemInput(net.minecraft.item.ItemStack input, boolean handleWildcard) {
        if (input == null) {
            return skipItemInput();
        }

        if (handleWildcard && ItemUtil.isWildcardItem(input)) {
            WildcardItem wildcardItem =
                    wildcardItemFactory.getWildcardItem(ItemUtil.getItemId(input));
            itemInputs.add(itemGroupFactory.getItemGroup(wildcardItem));
        } else {
            itemInputs.add(itemGroupFactory.getItemGroup(buildItemStack(input)));
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
        if (inputs == null) {
            return skipItemInput();
        }

        SortedSet<ItemStack> itemStacks = new TreeSet<>();
        SortedSet<WildcardItem> wildcardItems = new TreeSet<>();
        for (net.minecraft.item.ItemStack input : inputs) {
            if (handleWildcard && ItemUtil.isWildcardItem(input)) {
                wildcardItems.add(wildcardItemFactory.getWildcardItem(ItemUtil.getItemId(input)));
            } else {
                itemStacks.add(buildItemStack(input));
            }
        }
        itemInputs.add(itemGroupFactory.getItemGroup(itemStacks, wildcardItems));
        return this;
    }

    /** Adds all items in {@code inputs} into a single input slot, as an item group. */
    public RecipeBuilder addItemGroupInput(
            net.minecraft.item.ItemStack[] inputs, boolean handleWildcard) {
        if (inputs == null) {
            return skipItemInput();
        }

        return addItemGroupInput(Arrays.asList(inputs), handleWildcard);
    }

    public RecipeBuilder skipItemInput() {
        itemInputs.add(null);
        return this;
    }

    public RecipeBuilder addFluidInput(net.minecraftforge.fluids.FluidStack input) {
        if (input == null) {
            return skipFluidInput();
        }

        fluidInputs.add(fluidGroupFactory.getFluidGroup(buildFluidStack(input)));
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
        if (inputs == null) {
            return skipFluidInput();
        }

        SortedSet<FluidStack> fluidStacks =
                StreamSupport.stream(inputs.spliterator(), false)
                        .filter(Objects::nonNull)
                        .map(this::buildFluidStack)
                        .collect(Collectors.toCollection(TreeSet::new));
        fluidInputs.add(fluidGroupFactory.getFluidGroup(fluidStacks));
        return this;
    }

    /** Adds all fluids in {@code inputs} into a single input slot, as an fluid group. */
    public RecipeBuilder addFluidGroupInput(net.minecraftforge.fluids.FluidStack[] inputs) {
        if (inputs == null) {
            return skipFluidInput();
        }

        return addFluidGroupInput(Arrays.asList(inputs));
    }

    public RecipeBuilder skipFluidInput() {
        fluidInputs.add(null);
        return this;
    }

    public RecipeBuilder addItemOutput(net.minecraft.item.ItemStack output) {
        if (output == null) {
            return skipItemOutput();
        }

        itemOutputs.add(buildItemStack(output));
        return this;
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
        itemOutputs.add(null);
        return this;
    }

    public RecipeBuilder addFluidOutput(net.minecraftforge.fluids.FluidStack output) {
        if (output == null) {
            return skipFluidOutput();
        }

        fluidOutputs.add(buildFluidStack(output));
        return this;
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
        fluidOutputs.add(null);
        return this;
    }

    public Recipe build() {
        return recipeFactory.getRecipe(
                recipeType, itemInputs, fluidInputs, itemOutputs, fluidOutputs);
    }

    private ItemStack buildItemStack(net.minecraft.item.ItemStack itemStack) {
        return new ItemStack(itemFactory.getItem(itemStack), itemStack.stackSize);
    }

    private FluidStack buildFluidStack(net.minecraftforge.fluids.FluidStack fluidStack) {
        return new FluidStack(fluidFactory.getFluid(fluidStack), fluidStack.amount);
    }
}