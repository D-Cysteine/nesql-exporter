package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.exporter.proto.RecipeProto;
import com.github.dcysteine.nesql.sql.base.Fluid;
import com.github.dcysteine.nesql.sql.base.recipe.FluidGroup;
import com.github.dcysteine.nesql.sql.base.recipe.FluidStack;
import com.github.dcysteine.nesql.sql.base.Item;
import com.github.dcysteine.nesql.sql.base.recipe.ItemGroup;
import com.github.dcysteine.nesql.sql.base.recipe.ItemStack;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import jakarta.persistence.EntityManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final ItemGroupFactory itemGroupFactory;
    private final FluidGroupFactory fluidGroupFactory;
    private final RecipeFactory recipeFactory;
    private final RecipeType recipeType;
    private final List<SortedSet<ItemStack>> itemInputs;
    private final List<SortedSet<FluidStack>> fluidInputs;
    private final List<ItemStack> itemOutputs;
    private final List<FluidStack> fluidOutputs;

    public RecipeBuilder(EntityManager entityManager, RecipeType recipeType) {
        this.itemFactory = new ItemFactory(entityManager);
        this.fluidFactory = new FluidFactory(entityManager);
        this.itemGroupFactory = new ItemGroupFactory(entityManager);
        this.fluidGroupFactory = new FluidGroupFactory(entityManager);
        this.recipeFactory = new RecipeFactory(entityManager);
        this.recipeType = recipeType;
        this.itemInputs = new ArrayList<>();
        this.fluidInputs = new ArrayList<>();
        this.itemOutputs = new ArrayList<>();
        this.fluidOutputs = new ArrayList<>();
    }

    public RecipeBuilder addItemInput(net.minecraft.item.ItemStack input) {
        if (input == null) {
            return skipItemInput();
        }

        SortedSet<ItemStack> sortedSet = new TreeSet<>();
        sortedSet.add(buildItemStack(input));
        itemInputs.add(sortedSet);
        return this;
    }

    /** Adds each item in {@code inputs} into a separate input slot. */
    public RecipeBuilder addAllItemInput(Iterable<net.minecraft.item.ItemStack> inputs) {
        inputs.forEach(this::addItemInput);
        return this;
    }

    /** Adds each item in {@code inputs} into a separate input slot. */
    public RecipeBuilder addAllItemInput(net.minecraft.item.ItemStack[] inputs) {
        Stream.of(inputs).forEach(this::addItemInput);
        return this;
    }

    /** Adds all items in {@code inputs} into a single input slot, as an item group. */
    public RecipeBuilder addItemGroupInput(Iterable<net.minecraft.item.ItemStack> inputs) {
        if (inputs == null) {
            return skipItemInput();
        }

        itemInputs.add(
                StreamSupport.stream(inputs.spliterator(), false)
                        .filter(Objects::nonNull)
                        .map(this::buildItemStack)
                        .collect(Collectors.toCollection(TreeSet::new)));
        return this;
    }

    /** Adds all items in {@code inputs} into a single input slot, as an item group. */
    public RecipeBuilder addItemGroupInput(net.minecraft.item.ItemStack[] inputs) {
        if (inputs == null) {
            return skipItemInput();
        }

        return addItemGroupInput(Arrays.asList(inputs));
    }

    public RecipeBuilder skipItemInput() {
        itemInputs.add(null);
        return this;
    }

    public RecipeBuilder addFluidInput(net.minecraftforge.fluids.FluidStack input) {
        if (input == null) {
            return skipFluidInput();
        }

        SortedSet<FluidStack> sortedSet = new TreeSet<>();
        sortedSet.add(buildFluidStack(input));
        fluidInputs.add(sortedSet);
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

        fluidInputs.add(
                StreamSupport.stream(inputs.spliterator(), false)
                        .filter(Objects::nonNull)
                        .map(this::buildFluidStack)
                        .collect(Collectors.toCollection(TreeSet::new)));
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
        List<ItemGroup> itemGroups = new ArrayList<>();
        List<FluidGroup> fluidGroups = new ArrayList<>();

        RecipeProto.RecipePb.Builder builder = RecipeProto.RecipePb.newBuilder()
                .setRecipeType(recipeType.ordinal());
        itemOutputs.forEach(itemStack -> builder.addItemOutput(buildItemPb(itemStack)));
        fluidOutputs.forEach(fluidStack -> builder.addFluidOutput(buildFluidPb(fluidStack)));

        for (SortedSet<ItemStack> itemStacks : itemInputs) {
            if (itemStacks == null) {
                itemGroups.add(null);
                continue;
            }

            RecipeProto.ItemGroupPb itemGroupPb = buildItemGroupPb(itemStacks);
            builder.addItemInput(itemGroupPb);

            ItemGroup itemGroup =
                    itemGroupFactory.getItemGroup(IdUtil.encodeProto(itemGroupPb), itemStacks);
            itemGroups.add(itemGroup);
        }
        for (SortedSet<FluidStack> fluidStacks : fluidInputs) {
            if (fluidStacks == null) {
                fluidGroups.add(null);
                continue;
            }

            RecipeProto.FluidGroupPb fluidGroupPb = buildFluidGroupPb(fluidStacks);
            builder.addFluidInput(fluidGroupPb);

            FluidGroup fluidGroup =
                    fluidGroupFactory.getFluidGroup(IdUtil.encodeProto(fluidGroupPb), fluidStacks);
            fluidGroups.add(fluidGroup);
        }

        String id = IdUtil.encodeProto(builder.build());
        return recipeFactory.getRecipe(
                id, recipeType, itemGroups, fluidGroups, itemOutputs, fluidOutputs);
    }

    private ItemStack buildItemStack(net.minecraft.item.ItemStack itemStack) {
        return new ItemStack(itemFactory.getItem(itemStack), itemStack.stackSize);
    }

    private FluidStack buildFluidStack(net.minecraftforge.fluids.FluidStack fluidStack) {
        return new FluidStack(fluidFactory.getFluid(fluidStack), fluidStack.amount);
    }

    private static RecipeProto.ItemGroupPb buildItemGroupPb(SortedSet<ItemStack> itemStacks) {
        RecipeProto.ItemGroupPb.Builder builder = RecipeProto.ItemGroupPb.newBuilder();
        itemStacks.forEach(itemStack -> builder.addItem(buildItemPb(itemStack)));
        return builder.build();
    }

    private static RecipeProto.FluidGroupPb buildFluidGroupPb(SortedSet<FluidStack> fluidStacks) {
        RecipeProto.FluidGroupPb.Builder builder = RecipeProto.FluidGroupPb.newBuilder();
        fluidStacks.forEach(fluidStack -> builder.addFluid(buildFluidPb(fluidStack)));
        return builder.build();
    }

    private static RecipeProto.ItemPb buildItemPb(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            return RecipeProto.ItemPb.getDefaultInstance();
        }

        Item item = itemStack.getItem();
        RecipeProto.ItemPb.Builder builder = RecipeProto.ItemPb.newBuilder()
                .setItemId(item.getItemId())
                .setDamage(item.getItemDamage())
                .setStackSize(itemStack.getStackSize());
        if (item.hasNbt()) {
            builder.setNbt(item.getNbt());
        }
        return builder.build();
    }

    private static RecipeProto.FluidPb buildFluidPb(@Nullable FluidStack fluidStack) {
        if (fluidStack == null) {
            return RecipeProto.FluidPb.getDefaultInstance();
        }

        Fluid fluid = fluidStack.getFluid();
        RecipeProto.FluidPb.Builder builder = RecipeProto.FluidPb.newBuilder()
                .setFluidId(fluid.getFluidId())
                .setAmount(fluidStack.getAmount());
        if (fluid.hasNbt()) {
            builder.setNbt(fluid.getNbt());
        }
        return builder.build();
    }
}