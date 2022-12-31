package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.exporter.util.render.RenderDispatcher;
import com.github.dcysteine.nesql.exporter.util.render.RenderJob;
import com.github.dcysteine.nesql.exporter.util.render.Renderer;
import com.github.dcysteine.nesql.sql.base.Fluid;
import jakarta.persistence.EntityManager;
import net.minecraftforge.fluids.FluidStack;

public class FluidFactory extends EntityFactory<Fluid, String> {
    public FluidFactory(EntityManager entityManager) {
        super(entityManager);
    }

    public Fluid getFluid(FluidStack fluidStack) {
        Fluid fluid = new Fluid(
                IdUtil.fluidId(fluidStack),
                IdUtil.imageFilePath(fluidStack),
                fluidStack.getFluid().getName(),
                fluidStack.getUnlocalizedName(),
                fluidStack.getLocalizedName(),
                fluidStack.getFluidID(),
                fluidStack.tag == null ? null : fluidStack.tag.toString());

        String renderedFluidKey = IdUtil.fluidId(fluidStack.getFluid());
        if (ConfigOptions.RENDER_ICONS.get()
                && Renderer.INSTANCE.isUnrenderedFluid(renderedFluidKey)) {
            RenderDispatcher.INSTANCE.addJob(RenderJob.ofFluid(fluidStack));
        }
        return findOrPersist(Fluid.class, fluid);
    }
}
