package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStackWithProbability;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStackWithProbability;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/** Helper class that builds {@link Recipe} instances. */
public class RecipeBuilder extends PluginHelper {
    private final ItemFactory itemFactory;
    private final FluidFactory fluidFactory;
    private final ItemGroupFactory itemGroupFactory;
    private final FluidGroupFactory fluidGroupFactory;
    private final RecipeFactory recipeFactory;
    private final RecipeType recipeType;
    private final Map<Integer, ItemGroup> itemInputs;
    private final Map<Integer, FluidGroup> fluidInputs;
    private final Map<Integer, ItemStackWithProbability> itemOutputs;
    private final Map<Integer, FluidStackWithProbability> fluidOutputs;
    private int itemInputsIndex;
    private int fluidInputsIndex;
    private int itemOutputsIndex;
    private int fluidOutputsIndex;

    public RecipeBuilder(PluginExporter exporter, RecipeType recipeType) {
        super(exporter);
        this.itemFactory = new ItemFactory(exporter);
        this.fluidFactory = new FluidFactory(exporter);
        this.itemGroupFactory = new ItemGroupFactory(exporter);
        this.fluidGroupFactory = new FluidGroupFactory(exporter);
        this.recipeFactory = new RecipeFactory(exporter);
        this.recipeType = recipeType;
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
        if (input == null) {
            return skipItemInput();
        }

        itemInputs.put(itemInputsIndex++, itemGroupFactory.get(input, handleWildcard));
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
        if (inputs.stream().allMatch(Objects::isNull)) {
            return skipItemInput();
        }

        itemInputs.put(itemInputsIndex++, itemGroupFactory.get(inputs, handleWildcard));
        return this;
    }

    /** Adds all items in {@code inputs} into a single input slot, as an item group. */
    public RecipeBuilder addItemGroupInput(
            net.minecraft.item.ItemStack[] inputs, boolean handleWildcard) {
        return addItemGroupInput(Arrays.asList(inputs), handleWildcard);
    }

    public RecipeBuilder skipItemInput() {
        if (recipeType.isShapeless()) {
            logger.warn("Skipping item input index for shapeless recipe!");
        }

        itemInputsIndex++;
        return this;
    }

    public RecipeBuilder addFluidInput(net.minecraftforge.fluids.FluidStack input) {
        if (input == null) {
            return skipFluidInput();
        }

        fluidInputs.put(fluidInputsIndex++, fluidGroupFactory.get(input));
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

    /** Adds all fluids in {@code inputs} into a single input slot, as a fluid group. */
    public RecipeBuilder addFluidGroupInput(
            Collection<net.minecraftforge.fluids.FluidStack> inputs) {
        if (inputs.stream().allMatch(Objects::isNull)) {
            return skipFluidInput();
        }

        fluidInputs.put(fluidInputsIndex++, fluidGroupFactory.get(inputs));
        return this;
    }

    /** Adds all fluids in {@code inputs} into a single input slot, as a fluid group. */
    public RecipeBuilder addFluidGroupInput(net.minecraftforge.fluids.FluidStack[] inputs) {
        return addFluidGroupInput(Arrays.asList(inputs));
    }

    public RecipeBuilder skipFluidInput() {
        if (recipeType.isShapeless()) {
            logger.warn("Skipping fluid input index for shapeless recipe!");
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
        if (recipeType.isShapeless()) {
            logger.warn("Skipping item output index for shapeless recipe!");
        }

        itemOutputsIndex++;
        return this;
    }

    public RecipeBuilder addFluidOutput(
            net.minecraftforge.fluids.FluidStack output, double probability) {
        if (output == null) {
            return skipFluidOutput();
        }

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
        if (recipeType.isShapeless()) {
            logger.warn("Skipping fluid output index for shapeless recipe!");
        }

        fluidOutputsIndex++;
        return this;
    }

    public Recipe build() {
        return recipeFactory.get(
                recipeType, itemInputs, fluidInputs, itemOutputs, fluidOutputs);
    }

    private ItemStackWithProbability buildItemStackWithProbability(
            net.minecraft.item.ItemStack itemStack, double probability) {
        return new ItemStackWithProbability(
                itemFactory.get(itemStack), itemStack.stackSize, probability);
    }

    private FluidStackWithProbability buildFluidStackWithProbability(
            net.minecraftforge.fluids.FluidStack fluidStack, double probability) {
        return new FluidStackWithProbability(
                fluidFactory.get(fluidStack), fluidStack.amount, probability);
    }
}