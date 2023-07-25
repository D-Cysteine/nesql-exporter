package com.github.dcysteine.nesql.exporter.util.render;

import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.google.auto.value.AutoOneOf;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Note: this class doesn't quite conform to the {@link AutoOneOf} contract, as some of its contents
 * ({@link ItemStack}) are mutable. Shouldn't matter for our limited use-case, though.
 */
@AutoOneOf(RenderJob.JobType.class)
public abstract class RenderJob {
    public enum JobType {
        ITEM, FLUID, ENTITY
    }

    public static RenderJob ofItem(ItemStack itemStack) {
        ItemStack newStack = itemStack.copy();
        newStack.stackSize = 1;
        return AutoOneOf_RenderJob.item(newStack);
    }

    public static RenderJob ofFluid(FluidStack fluidStack) {
        return AutoOneOf_RenderJob.fluid(fluidStack);
    }

    public static RenderJob ofEntity(Entity entity) {
        return AutoOneOf_RenderJob.entity(entity);
    }

    public abstract JobType getType();
    public abstract ItemStack getItem();
    public abstract FluidStack getFluid();
    public abstract Entity getEntity();

    public String getImageFilePath() {
        switch (getType()) {
            case ITEM:
                return IdUtil.imageFilePath(getItem());

            case FLUID:
                return IdUtil.imageFilePath(getFluid());

            case ENTITY:
                return IdUtil.imageFilePath(getEntity());

            default:
                throw new IllegalStateException("Unhandled job type: " + this);
        }
    }
}
