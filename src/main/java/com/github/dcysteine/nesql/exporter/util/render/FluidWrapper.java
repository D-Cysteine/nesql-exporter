package com.github.dcysteine.nesql.exporter.util.render;

import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.google.auto.value.AutoValue;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/** Wrapper for fluids that contains additional information. */
@AutoValue
public abstract class FluidWrapper {
    public static FluidWrapper create(FluidStack fluidStack) {
        return new AutoValue_FluidWrapper(fluidStack.getFluid(), IdUtil.imageFilePath(fluidStack));
    }

    public abstract Fluid fluid();

    public abstract String imageFilePath();
}
