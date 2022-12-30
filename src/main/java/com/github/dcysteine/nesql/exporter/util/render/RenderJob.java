package com.github.dcysteine.nesql.exporter.util.render;

import com.google.auto.value.AutoOneOf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@AutoOneOf(RenderJob.JobType.class)
public abstract class RenderJob {
    public enum JobType {
        ITEM, FLUID
    }

    public static RenderJob ofItem(ItemStack itemStack) {
        return AutoOneOf_RenderJob.item(ItemWrapper.create(itemStack));
    }

    public static RenderJob ofFluid(FluidStack fluidStack) {
        return AutoOneOf_RenderJob.fluid(FluidWrapper.create(fluidStack));
    }

    public abstract JobType type();
    public abstract ItemWrapper item();
    public abstract FluidWrapper fluid();
}
