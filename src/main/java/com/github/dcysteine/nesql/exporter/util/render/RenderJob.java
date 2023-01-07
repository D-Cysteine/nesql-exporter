package com.github.dcysteine.nesql.exporter.util.render;

import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.google.auto.value.AutoOneOf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Note: this class doesn't quite conform to the {@link AutoOneOf} contract, as some of its contents
 * ({@link ItemStack}) are mutable. Shouldn't matter for our limited use-case, though.
 */
@AutoOneOf(RenderJob.JobType.class)
public abstract class RenderJob {
    public enum JobType {
        ITEM, FLUID
    }

    public static RenderJob ofItem(ItemStack itemStack) {
        ItemStack newStack = itemStack.copy();
        newStack.stackSize = 1;
        return AutoOneOf_RenderJob.item(newStack);
    }

    public static RenderJob ofFluid(FluidStack fluidStack) {
        return AutoOneOf_RenderJob.fluid(fluidStack);
    }

    public abstract JobType getType();
    public abstract ItemStack getItem();
    public abstract FluidStack getFluid();

    public String getImageFilePath() {
        switch (getType()) {
            case ITEM:
                return IdUtil.imageFilePath(getItem());

            case FLUID:
                return IdUtil.imageFilePath(getFluid());

            default:
                throw new IllegalStateException("Unhandled job type: " + this);
        }
    }
}
