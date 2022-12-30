package com.github.dcysteine.nesql.exporter.plugin.base;

import com.github.dcysteine.nesql.exporter.plugin.Processor;
import com.github.dcysteine.nesql.exporter.util.EntitySaver;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class RecipeProcessor implements Processor {
    private final EntitySaver entitySaver;

    public RecipeProcessor(EntitySaver entitySaver) {
        this.entitySaver = entitySaver;
    }

    @Override
    public void process() {
        entitySaver.saveItem(new ItemStack(Items.blaze_rod));
        entitySaver.saveItem(new ItemStack(Items.feather));
        entitySaver.saveItem(new ItemStack(Blocks.wool, 10, 5));

        entitySaver.saveFluid(new FluidStack(FluidRegistry.getFluid("water"), 5));
        entitySaver.saveFluid(new FluidStack(FluidRegistry.getFluid("lava"), 10));
    }
}
