package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.exporter.util.ItemUtil;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.exporter.util.render.RenderDispatcher;
import com.github.dcysteine.nesql.exporter.util.render.RenderJob;
import com.github.dcysteine.nesql.exporter.util.render.Renderer;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import jakarta.persistence.EntityManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidFactory extends EntityFactory<Fluid, String> {
    public FluidFactory(EntityManager entityManager) {
        super(entityManager);
    }

    public Fluid getFluid(FluidStack fluidStack) {
        String uniqueName = FluidRegistry.getDefaultFluidName(fluidStack.getFluid());
        int separator = uniqueName.indexOf(':');
        String modId = uniqueName.substring(0, separator);
        String internalName = uniqueName.substring(separator + 1);

        // We're just exporting data, not actually doing recipe matching, so I think we can just
        // ignore wildcard NBT. It probably isn't handled by most recipe types, anyway.
        String nbt = "";
        if (fluidStack.tag != null && !ItemUtil.isWildcardNbt(fluidStack.tag)) {
            nbt = fluidStack.tag.toString();
        }

        Fluid fluid = new Fluid(
                IdPrefixUtil.FLUID.applyPrefix(IdUtil.fluidId(fluidStack)),
                StringUtil.formatFilePath(IdUtil.imageFilePath(fluidStack)),
                modId,
                internalName,
                fluidStack.getUnlocalizedName(),
                StringUtil.stripFormatting(fluidStack.getLocalizedName()),
                fluidStack.getFluidID(),
                nbt,
                fluidStack.getFluid().getLuminosity(fluidStack),
                fluidStack.getFluid().getDensity(fluidStack),
                fluidStack.getFluid().getTemperature(fluidStack),
                fluidStack.getFluid().getViscosity(fluidStack),
                fluidStack.getFluid().isGaseous(fluidStack));

        if (fluidStack.getFluid().getIcon() == null) {
            Logger.BASE.error("Found fluid with null icon: {}", fluid.getId());
        } else {
            String renderedFluidKey = IdUtil.fluidId(fluidStack.getFluid());
            if (ConfigOptions.RENDER_ICONS.get()
                    && Renderer.INSTANCE.isUnrenderedFluid(renderedFluidKey)) {
                Logger.intermittentLog(
                        Logger.BASE,
                        "Enqueueing render of fluid #{}: " + renderedFluidKey,
                        Renderer.INSTANCE.getRenderedFluidCount());
                RenderDispatcher.INSTANCE.addJob(RenderJob.ofFluid(fluidStack));
            }
        }

        return findOrPersist(Fluid.class, fluid);
    }
}
