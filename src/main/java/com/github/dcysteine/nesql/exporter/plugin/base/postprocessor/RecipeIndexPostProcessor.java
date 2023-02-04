package com.github.dcysteine.nesql.exporter.plugin.base.postprocessor;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.util.QueryUtil;
import com.github.dcysteine.nesql.sql.base.fluid.FluidGroup;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;

import java.util.List;
import java.util.Set;

/**
 * Post-processor which builds item and fluid input indices for recipes.
 *
 * <p>This post-processor must be run after {@link WildcardItemGroupPostProcessor}, as it needs
 * wildcard item groups to be resolved.
 */
public class RecipeIndexPostProcessor extends PluginHelper {
    public RecipeIndexPostProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void postProcess() {
        List<Recipe> recipes = QueryUtil.getRecipes(entityManager);
        int total = recipes.size();
        logger.info("Post-processing {} recipes...", total);

        int count = 0;
        for (Recipe recipe : recipes) {
            count++;

            for (ItemGroup itemGroup : recipe.getItemInputs().values()) {
                itemGroup.getItemStacks().stream()
                        .map(ItemStack::getItem)
                        .forEach(recipe::addItemInputIndex);
                itemGroup.getResolvedWildcardItemStacks().stream()
                        .map(ItemStack::getItem)
                        .forEach(recipe::addItemInputIndex);
            }

            recipe.getFluidInputs().values().stream()
                    .map(FluidGroup::getFluidStacks)
                    .flatMap(Set::stream)
                    .map(FluidStack::getFluid)
                    .forEach(recipe::addFluidInputIndex);

            if (Logger.intermittentLog(count)) {
                logger.info("Post-processed recipe {} of {}", count, total);
                logger.info("Most recent recipe: {}", recipe.getId());
            }
        }

        logger.info("Finished post-processing recipes!");
    }
}
