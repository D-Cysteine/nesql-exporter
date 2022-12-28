package com.github.dcysteine.nesql.exporter.util.render;

import com.google.auto.value.AutoOneOf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

@AutoOneOf(RenderJob.JobType.class)
public abstract class RenderJob {
    public enum JobType {
        ITEM, FLUID
    }

    public static RenderJob ofItem(ItemStack item) {
        return AutoOneOf_RenderJob.item(ItemWrapper.create(item));
    }

    public static RenderJob ofItem(ItemWrapper item) {
        return AutoOneOf_RenderJob.item(item);
    }

    public static RenderJob ofFluid(Fluid fluid) {
        return AutoOneOf_RenderJob.fluid(fluid);
    }

    public abstract JobType type();
    public abstract ItemWrapper item();
    public abstract Fluid fluid();
}
