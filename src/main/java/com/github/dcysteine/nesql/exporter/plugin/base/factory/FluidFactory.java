package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.exporter.util.render.RenderDispatcher;
import com.github.dcysteine.nesql.exporter.util.render.RenderJob;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidFactory extends EntityFactory<Fluid, String> {
    public FluidFactory(PluginExporter exporter) {
        super(exporter);
    }

    public Fluid get(net.minecraftforge.fluids.Fluid fluid) {
        return get(new FluidStack(fluid, 1));
    }

    public Fluid get(FluidStack fluidStack) {
        String id = IdPrefixUtil.FLUID.applyPrefix(IdUtil.fluidId(fluidStack));
        Fluid fluid = entityManager.find(Fluid.class, id);
        if (fluid != null) {
            return fluid;
        }

        String uniqueName = FluidRegistry.getDefaultFluidName(fluidStack.getFluid());
        int separator = uniqueName.indexOf(':');
        String modId = uniqueName.substring(0, separator);
        String internalName = uniqueName.substring(separator + 1);

        String nbt = "";
        if (fluidStack.tag != null) {
            nbt = fluidStack.tag.toString();
            logger.warn("Found fluid stack with NBT: {}", fluidStack.getLocalizedName());
            logger.warn("NBT: {}", nbt);
        }

        fluid = new Fluid(
                id,
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
            logger.error("Found fluid with null icon: {}", fluid.getLocalizedName());
        } else {
            if (ConfigOptions.RENDER_ICONS.get()) {
                Logger.intermittentLog(
                        logger,
                        "Enqueueing render of fluid #{}: " + fluid.getLocalizedName(),
                        exporterState.incrementFluidCount());
                RenderDispatcher.INSTANCE.addJob(RenderJob.ofFluid(fluidStack));
            }
        }

        entityManager.persist(fluid);
        return fluid;
    }
}
