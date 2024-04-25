package com.github.dcysteine.nesql.exporter.plugin.gregtech;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.RecipeBuilder;
import com.github.dcysteine.nesql.exporter.plugin.gregtech.util.GregTechRecipeTypeHandler;
import com.github.dcysteine.nesql.exporter.plugin.gregtech.util.GregTechUtil;
import com.github.dcysteine.nesql.exporter.plugin.gregtech.util.RecipeMap;
import com.github.dcysteine.nesql.exporter.plugin.gregtech.util.Voltage;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import gregtech.api.util.GT_Recipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GregTechRecipeProcessor extends PluginHelper {
    private final GregTechRecipeTypeHandler recipeTypeHandler;

    public GregTechRecipeProcessor(
            PluginExporter exporter, GregTechRecipeTypeHandler recipeTypeHandler) {
        super(exporter);
        this.recipeTypeHandler = recipeTypeHandler;
    }

    public void process() {
        int recipeMapTotal = RecipeMap.values().length;
        logger.info("Processing {} GregTech recipe maps...", recipeMapTotal);

        GregTechRecipeFactory gregTechRecipeFactory = new GregTechRecipeFactory(exporter);
        int recipeMapCount = 0;
        for (RecipeMap recipeMap : RecipeMap.values()) {
            logger.info("Processing recipe map: " + recipeMap.getName());
            recipeMapCount++;

            Collection<GT_Recipe> recipes = recipeMap.getRecipeMap().getAllRecipes();
            int total = recipes.size();
            logger.info("Processing {} GregTech recipes...", total);

            int count = 0;
            for (GT_Recipe recipe : recipes) {
                count++;

                try {
                    int voltage = recipe.mEUt / recipeMap.getAmperage();
                    Voltage voltageTier = Voltage.convertVoltage(voltage);
                    RecipeType recipeType = recipeTypeHandler.getRecipeType(recipeMap, voltageTier);
                    RecipeBuilder builder = new RecipeBuilder(exporter, recipeType);
                    // TODO if we want to avoid skipping slots, esp. output slots, add null checks.
                    for (ItemStack input : recipe.mInputs) {
                        builder.addItemGroupInput(GregTechUtil.reverseUnify(input));
                    }
                    for (FluidStack input : recipe.mFluidInputs) {
                        builder.addFluidInput(input);
                    }
                    for (int i = 0; i < recipe.mOutputs.length; i++) {
                        ItemStack output = recipe.mOutputs[i];
                        int chance = recipe.getOutputChance(i);
                        if (chance == 100_00) {
                            builder.addItemOutput(output);
                        } else {
                            builder.addItemOutput(output, chance / 100_00d);
                        }
                    }
                    for (FluidStack output : recipe.mFluidOutputs) {
                        builder.addFluidOutput(output);
                    }

                    List<ItemStack> specialItems = new ArrayList<>();
                    if (recipe.mSpecialItems != null) {
                        specialItems = GregTechUtil.reverseUnify(recipe.mSpecialItems);
                    }

                    Recipe recipeEntity = builder.build();
                    gregTechRecipeFactory.get(
                            recipeEntity, recipeMap, recipe, voltageTier, voltage, specialItems);
                } catch (Exception e) {
                    // This try-catch is sadly necessary. There's a few weird exceptions that get
                    // thrown. There's even some that lack a stack trace!
                    logger.error("Caught exception processing GregTech recipe!", e);
                }

                if (Logger.intermittentLog(count)) {
                    logger.info(
                            "Processed GregTech {} recipe {} of {}",
                            recipeMap.getName(), count, total);
                }
            }

            exporterState.flushEntityManager();
            logger.info("Processed GregTech recipe map {} of {}", recipeMapCount, recipeMapTotal);
        }

        logger.info("Finished processing GregTech recipe maps!");
    }

}
