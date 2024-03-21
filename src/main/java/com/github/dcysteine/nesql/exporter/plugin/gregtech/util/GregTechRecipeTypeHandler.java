package com.github.dcysteine.nesql.exporter.plugin.gregtech.util;

import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.RecipeTypeFactory;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;

import java.util.Arrays;

public class GregTechRecipeTypeHandler extends PluginHelper {
    public static final String RECIPE_ID = "gregtech";
    public static final String RECIPE_CATEGORY = "gregtech";

    private final Table<GTRecipeMap, Voltage, RecipeType> recipeTypeTable;

    public GregTechRecipeTypeHandler(PluginExporter exporter) {
        super(exporter);
        recipeTypeTable =
                ArrayTable.create(
                        GTRecipeMap.allNEIRecipeMaps.values(), Arrays.asList(Voltage.values()));
    }

    public void initialize() {
        ItemFactory itemFactory = new ItemFactory(exporter);
        RecipeTypeFactory recipeTypeFactory = new RecipeTypeFactory(exporter);

        for (GTRecipeMap GTRecipeMap : GTRecipeMap.allNEIRecipeMaps.values()) {
            Item icon = itemFactory.get(GTRecipeMap.getIcon());
            for (Voltage voltage : Voltage.values()) {
                recipeTypeTable.put(
                        GTRecipeMap, voltage,
                        recipeTypeFactory.newBuilder()
                                .setId(RECIPE_ID, GTRecipeMap.getShortName(), voltage.getName())
                                .setCategory(RECIPE_CATEGORY)
                                .setType(GTRecipeMap.getName(voltage))
                                .setIcon(icon)
                                .setIconInfo(voltage.getName())
                                .setShapeless(GTRecipeMap.isShapeless())
                                .setItemInputDimension(GTRecipeMap.getItemInputDimension())
                                .setFluidInputDimension(GTRecipeMap.getFluidInputDimension())
                                .setItemOutputDimension(GTRecipeMap.getItemOutputDimension())
                                .setFluidOutputDimension(GTRecipeMap.getFluidOutputDimension())
                                .build());
            }
        }
    }

    public RecipeType getRecipeType(GTRecipeMap GTRecipeMap, Voltage voltage) {
        return recipeTypeTable.get(GTRecipeMap, voltage);
    }
}
