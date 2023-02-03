package com.github.dcysteine.nesql.exporter.plugin.gregtech;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.plugin.gregtech.util.RecipeMap;
import com.github.dcysteine.nesql.exporter.plugin.gregtech.util.Voltage;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import com.github.dcysteine.nesql.sql.gregtech.GregTechRecipe;
import com.google.common.base.Joiner;
import cpw.mods.fml.common.ModContainer;
import gregtech.api.util.GT_Recipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GregTechRecipeFactory extends EntityFactory<GregTechRecipe, String> {
    private final ItemFactory itemFactory;
    public GregTechRecipeFactory(PluginExporter exporter) {
        super(exporter);
        this.itemFactory = new ItemFactory(exporter);
    }

    public GregTechRecipe get(
            Recipe recipe, RecipeMap recipeMap, GT_Recipe gregTechRecipe,
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

        String additionalInfo = "";
        if (gregTechRecipe.getNeiDesc() != null) {
            additionalInfo = Joiner.on('\n').join(gregTechRecipe.getNeiDesc());
        }

        GregTechRecipe gregTechRecipeEntity =
                new GregTechRecipe(
                        id,
                        recipe,
                        voltageTier.getName(),
                        voltage,
                        recipeMap.getAmperage(),
                        gregTechRecipe.mDuration,
                        requiresCleanroom,
                        requiresLowGravity,
                        specialItemEntities,
                        modOwners,
                        additionalInfo);
        return findOrPersist(GregTechRecipe.class, gregTechRecipeEntity);
    }
}
