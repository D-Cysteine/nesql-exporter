package com.github.dcysteine.nesql.exporter.plugin.base.postprocessor;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.util.QueryUtil;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/** Post-processor which builds item and fluid input indices for recipes. */
public class RecipePostProcessor extends PluginHelper {
    public RecipePostProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void postProcess() {
        long total = QueryUtil.countRecipes(entityManager);
        logger.info("Post-processing {} recipes...", total);

        // Need to use AtomicInteger so that we can mutate it within a lambda.
        AtomicInteger count = new AtomicInteger();
        QueryUtil.getRecipes(entityManager).forEach(
                recipe -> {
                    recipe.getItemInputs().values().stream()
                            .map(ItemGroup::getItemStacks)
                            .flatMap(Set::stream)
                            .map(ItemStack::getItem)
                            .forEach(recipe::addItemInputsItem);

                    recipe.getFluidInputs().values().stream()
                            .map(FluidGroup::getFluidStacks)
                            .flatMap(Set::stream)
                            .map(FluidStack::getFluid)
                            .forEach(recipe::addFluidInputsFluid);

                    if (Logger.intermittentLog(count.incrementAndGet())) {
                        logger.info("Post-processed recipe {} of {}", count.get(), total);
                        logger.info("Most recent recipe: {}", recipe.getId());
                    }
                });

        exporterState.flushEntityManager();
        logger.info("Finished post-processing recipes!");
    }
}
