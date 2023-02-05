package com.github.dcysteine.nesql.exporter.plugin.quest;

import betterquesting.api.utils.BigItemStack;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.FluidFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemGroupFactory;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestUtil {
    // Static class.
    private QuestUtil() {}

    public static List<ItemGroup> buildItems(
            ItemGroupFactory itemGroupFactory, List<BigItemStack> items) {
        return items.stream()
                .map(
                        bigItemStack ->
                                itemGroupFactory.get(
                                        bigItemStack.getBaseStack(), bigItemStack.stackSize, true))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<FluidStack> buildFluids(
            FluidFactory fluidFactory, List<net.minecraftforge.fluids.FluidStack> fluids) {
        return fluids.stream()
                .map(
                        fluidStack ->
                                new FluidStack(
                                        fluidFactory.get(fluidStack), fluidStack.amount))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
