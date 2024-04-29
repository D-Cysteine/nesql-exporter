package com.github.dcysteine.nesql.exporter.render;

import com.github.dcysteine.nesql.exporter.common.MobSpec;
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
        ITEM, FLUID, MOB
    }

    public static RenderJob ofItem(ItemStack itemStack) {
        ItemStack newStack = itemStack.copy();
        newStack.stackSize = 1;
        return AutoOneOf_RenderJob.item(newStack);
    }

    public static RenderJob ofFluid(FluidStack fluidStack) {
        return AutoOneOf_RenderJob.fluid(fluidStack);
    }

    public static RenderJob ofMob(MobSpec spec) {
        return AutoOneOf_RenderJob.mob(spec);
    }

    public abstract JobType getType();
    public abstract ItemStack getItem();
    public abstract FluidStack getFluid();
    public abstract MobSpec getMob();

    public Renderer.RenderTarget getRenderTarget() {
        switch (getType()) {
            case ITEM:
            case FLUID:
                return Renderer.RenderTarget.ICON;

            case MOB:
                return Renderer.RenderTarget.MOB;

            default:
                throw new IllegalStateException("Unhandled job type: " + this);
        }
    }

    public String getImageFilePath() {
        switch (getType()) {
            case ITEM:
                return IdUtil.imageFilePath(getItem());

            case FLUID:
                return IdUtil.imageFilePath(getFluid());

            case MOB:
                return IdUtil.mobImageFilePath(getMob());

            default:
                throw new IllegalStateException("Unhandled job type: " + this);
        }
    }
}
