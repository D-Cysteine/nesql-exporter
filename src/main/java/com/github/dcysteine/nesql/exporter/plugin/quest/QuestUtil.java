package com.github.dcysteine.nesql.exporter.plugin.quest;

import betterquesting.api.utils.BigItemStack;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.FluidFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestUtil {
    // Static class.
    private QuestUtil() {}

    public static List<ItemStack> buildItemStacks(
            ItemFactory itemFactory, List<BigItemStack> items) {
        // TODO this doesn't handle ore dict; add support if we need it.
        return items.stream()
                .map(
                        bigItemStack ->
                                new ItemStack(
                                        itemFactory.get(bigItemStack.getBaseStack()),
                                        bigItemStack.stackSize))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<FluidStack> buildFluidStacks(
            FluidFactory fluidFactory, List<net.minecraftforge.fluids.FluidStack> fluids) {
        return fluids.stream()
                .map(
                        fluidStack ->
                                new FluidStack(
                                        fluidFactory.get(fluidStack), fluidStack.amount))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
