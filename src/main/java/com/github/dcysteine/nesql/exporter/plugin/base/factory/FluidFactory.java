package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.exporter.util.ItemUtil;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.exporter.util.render.RenderDispatcher;
import com.github.dcysteine.nesql.exporter.util.render.RenderJob;
import com.github.dcysteine.nesql.exporter.util.render.Renderer;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import jakarta.persistence.EntityManager;
import net.minecraftforge.fluids.FluidStack;

public class FluidFactory extends EntityFactory<Fluid, String> {
    public FluidFactory(EntityManager entityManager) {
        super(entityManager);
    }

    public Fluid getFluid(FluidStack fluidStack) {
        // We're just exporting data, not actually doing recipe matching, so I think we can just
        // ignore wildcard NBT. It probably isn't handled by most recipe types, anyway.
        String nbt = null;
        if (fluidStack.tag != null && !ItemUtil.isWildcardNbt(fluidStack.tag)) {
            nbt = fluidStack.tag.toString();
        }

        Fluid fluid = new Fluid(
                IdUtil.fluidId(fluidStack),
                IdUtil.imageFilePath(fluidStack),
                fluidStack.getFluid().getName(),
                fluidStack.getUnlocalizedName(),
                StringUtil.stripFormatting(fluidStack.getLocalizedName()),
                fluidStack.getFluidID(),
                nbt);

        if (fluidStack.getFluid().getIcon() == null) {
            Logger.MOD.error("Found fluid with null icon: {}", fluid.getId());
        } else {
            String renderedFluidKey = IdUtil.fluidId(fluidStack.getFluid());
            if (Renderer.INSTANCE.isUnrenderedFluid(renderedFluidKey)
                    && ConfigOptions.RENDER_ICONS.get()) {
                if (Logger.intermittentLog(
                        "Enqueueing render of fluid #{}: " + renderedFluidKey,
                        Renderer.INSTANCE.getRenderedFluidCount())) {
                    Logger.MOD.info(
                            "Remaining render jobs: " + RenderDispatcher.INSTANCE.getJobCount());
                }

                RenderDispatcher.INSTANCE.addJob(RenderJob.ofFluid(fluidStack));
            }
        }

        return findOrPersist(Fluid.class, fluid);
    }
}
