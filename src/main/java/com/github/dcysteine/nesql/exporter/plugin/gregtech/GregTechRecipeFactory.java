package com.github.dcysteine.nesql.exporter.plugin.gregtech;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.plugin.gregtech.util.GregTechRecipeMap;
import com.github.dcysteine.nesql.exporter.plugin.gregtech.util.Voltage;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.NumberUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.gregtech.GregTechRecipe;
import com.google.common.base.Joiner;
import cpw.mods.fml.common.ModContainer;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.util.GT_Recipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GregTechRecipeFactory extends EntityFactory<GregTechRecipe, String> {
    private final ItemFactory itemFactory;

    public GregTechRecipeFactory(PluginExporter exporter) {
        super(exporter);
        this.itemFactory = new ItemFactory(exporter);
    }

    public GregTechRecipe get(
            Recipe recipe, GregTechRecipeMap GregTechRecipeMap, GT_Recipe gregTechRecipe,
            Voltage voltageTier, int voltage, List<ItemStack> specialItems) {
        String id = IdPrefixUtil.GREG_TECH_RECIPE.applyPrefix(recipe.getId());

        boolean requiresCleanroom =
                gregTechRecipe.mSpecialValue == -200 || gregTechRecipe.mSpecialValue == -300;
        boolean requiresLowGravity =
                gregTechRecipe.mSpecialValue == -100 || gregTechRecipe.mSpecialValue == -300;

        List<Item> specialItemEntities =
                specialItems.stream()
                        .map(itemFactory::get)
                        .collect(Collectors.toCollection(ArrayList::new));

        List<String> modOwners =
                gregTechRecipe.owners.stream()
                        .map(ModContainer::getModId)
                        .collect(Collectors.toCollection(ArrayList::new));

        List<String> additionalInfo = new ArrayList<>();
        switch (GregTechRecipeMap.getShortName()) {
            case "gt.recipe.fusionreactor": {
                // Special handling for fusion recipes.
                int euToStart = gregTechRecipe.mSpecialValue;

                int euTier;
                if (euToStart <= 160_000_000) {
                    euTier = 1;
                } else if (euToStart <= 320_000_000) {
                    euTier = 2;
                } else if (euToStart <= 640_000_000) {
                    euTier = 3;
                } else {
                    euTier = 4;
                }

                int vTier;
                if (voltage <= GT_Values.V[6]) {
                    vTier = 1;
                } else if (voltage <= GT_Values.V[7]) {
                    vTier = 2;
                } else if (voltage <= GT_Values.V[8]) {
                    vTier = 3;
                } else {
                    vTier = 4;
                }

                additionalInfo.add(
                        String.format(
                                "To start: %s EU (MK %d)",
                                NumberUtil.formatInteger(euToStart),
                                Math.max(euTier, vTier)));
                break;
            }

            case "gt.recipe.blastfurnace":
            case "gt.recipe.plasmaforge": {
                // Special handling for EBF and DTPF recipes.
                int heat = gregTechRecipe.mSpecialValue;

                String tier = HeatingCoilLevel.MAX.getName();
                for (HeatingCoilLevel heatLevel : HeatingCoilLevel.values()) {
                    if (heatLevel == HeatingCoilLevel.None || heatLevel == HeatingCoilLevel.ULV) {
                        continue;
                    }
                    if (heat <= heatLevel.getHeat()) {
                        tier = heatLevel.getName();
                        break;
                    }
                }

                additionalInfo.add(
                        String.format(
                                "Heat capacity: %sK (%s)", NumberUtil.formatInteger(heat), tier));
                break;
            }
        }
        if (gregTechRecipe.getNeiDesc() != null) {
            additionalInfo.addAll(Arrays.asList(gregTechRecipe.getNeiDesc()));
        }

        GregTechRecipe gregTechRecipeEntity =
                new GregTechRecipe(
                        id,
                        recipe,
                        voltageTier.getName(),
                        voltage,
                        GregTechRecipeMap.getAmperage(),
                        gregTechRecipe.mDuration,
                        requiresCleanroom,
                        requiresLowGravity,
                        specialItemEntities,
                        modOwners,
                        Joiner.on('\n').join(additionalInfo));
        return findOrPersist(GregTechRecipe.class, gregTechRecipeEntity);
    }
}
