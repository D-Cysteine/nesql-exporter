package com.github.dcysteine.nesql.exporter.processor.base;

import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.processor.Processor;
import com.github.dcysteine.nesql.exporter.util.render.RenderDispatcher;
import com.github.dcysteine.nesql.exporter.util.render.RenderJob;
import com.github.dcysteine.nesql.sql.repository.EntitySaver;
import com.github.dcysteine.nesql.sql.repository.base.fluid.FluidRepository;
import com.github.dcysteine.nesql.sql.repository.base.fluid.FluidRow;
import com.github.dcysteine.nesql.sql.repository.base.item.ItemRepository;
import com.github.dcysteine.nesql.sql.repository.base.item.ItemRow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.persistence.EntityManager;

public class RecipeProcessor implements Processor {
    private final EntitySaver<ItemRepository, ItemRow> itemSaver;
    private final EntitySaver<FluidRepository, FluidRow> fluidSaver;

    public RecipeProcessor(EntityManager entityManager) {
        this.itemSaver = new EntitySaver<>(entityManager, ItemRepository.class);
        this.fluidSaver = new EntitySaver<>(entityManager, FluidRepository.class);
    }

    @Override
    public void process() {
        processItemStack(new ItemStack(Items.blaze_rod));
        processItemStack(new ItemStack(Items.feather));
        processItemStack(new ItemStack(Blocks.wool, 10, 5));

        processFluidStack(new FluidStack(FluidRegistry.getFluid("water"), 5));
        processFluidStack(new FluidStack(FluidRegistry.getFluid("lava"), 10));
    }

    // TODO move this to a common class
    private void processItemStack(ItemStack itemStack) {
        if (ConfigOptions.RENDER_ICONS.get()) {
            RenderDispatcher.INSTANCE.addJob(RenderJob.ofItem(itemStack));
        }
        itemSaver.save(new ItemRow(itemStack));
    }

    // TODO move this to a common class
    private void processFluidStack(FluidStack fluidStack) {
        if (ConfigOptions.RENDER_ICONS.get()) {
            RenderDispatcher.INSTANCE.addJob(RenderJob.ofFluid(fluidStack.getFluid()));
        }
        fluidSaver.save(new FluidRow(fluidStack));
    }
}
