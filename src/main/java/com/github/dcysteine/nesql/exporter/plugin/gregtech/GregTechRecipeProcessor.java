package com.github.dcysteine.nesql.exporter.plugin.gregtech;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.RecipeBuilder;
import com.github.dcysteine.nesql.exporter.plugin.gregtech.util.GTRecipeMap;
import com.github.dcysteine.nesql.exporter.plugin.gregtech.util.GregTechRecipeTypeHandler;
import com.github.dcysteine.nesql.exporter.plugin.gregtech.util.GregTechUtil;
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
        int recipeMapTotal = GTRecipeMap.allNEIRecipeMaps.values().size();
        logger.info("Processing {} GregTech recipe maps...", recipeMapTotal);

        GregTechRecipeFactory gregTechRecipeFactory = new GregTechRecipeFactory(exporter);
        int recipeMapCount = 0;
        for (GTRecipeMap GTRecipeMap : GTRecipeMap.allNEIRecipeMaps.values()) {
            logger.info("Processing recipe map: " + GTRecipeMap.getName());
            recipeMapCount++;

            Collection<GT_Recipe> recipes = GTRecipeMap.getRecipeMap().getAllRecipes();
            int total = recipes.size();
            logger.info("Processing {} GregTech recipes...", total);

            int count = 0;
            for (GT_Recipe recipe : recipes) {
                count++;

                try {
                    int voltage = recipe.mEUt / GTRecipeMap.getAmperage();
                    Voltage voltageTier = Voltage.convertVoltage(voltage);
                    RecipeType recipeType = recipeTypeHandler.getRecipeType(GTRecipeMap, voltageTier);
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
                            recipeEntity, GTRecipeMap, recipe, voltageTier, voltage, specialItems);
                } catch (Exception e) {
                    logger.error("Caught exception processing GregTech recipe!");
                    e.printStackTrace();
                }

                if (Logger.intermittentLog(count)) {
                    logger.info(
                            "Processed GregTech {} recipe {} of {}",
                            GTRecipeMap.getName(), count, total);
                }
            }

            exporterState.flushEntityManager();
            logger.info("Processed GregTech recipe map {} of {}", recipeMapCount, recipeMapTotal);
        }

        logger.info("Finished processing GregTech recipe maps!");
    }

}
