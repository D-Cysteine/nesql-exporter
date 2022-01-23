package com.github.dcysteine.nesql.exporter.handler.forestry;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.toprettystring.ToPrettyString;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Optional;

@AutoValue
public abstract class Stack {
    public static Stack create(FluidStack fluidStack) {
        return new AutoValue_Stack(
                fluidStack.getLocalizedName(), fluidStack.amount, Optional.empty());
    }

    public static Stack create(ItemStack itemStack) {
        return new AutoValue_Stack(
                itemStack.getDisplayName(), itemStack.stackSize, Optional.empty());
    }

    public static Stack create(ItemStack itemStack, float chance) {
        return new AutoValue_Stack(
                itemStack.getDisplayName(), itemStack.stackSize, Optional.of(chance));
    }

    public abstract String name();
    public abstract int amount();

    /**
     * This will be empty optional for stacks in recipe inputs.
     */
    public abstract Optional<Float> chance();

    @ToPrettyString
    @Override
    public abstract String toString();
}
