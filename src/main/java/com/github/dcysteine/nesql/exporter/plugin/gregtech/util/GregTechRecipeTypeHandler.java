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

    private final Table<RecipeMap, Voltage, RecipeType> recipeTypeTable;

    public GregTechRecipeTypeHandler(PluginExporter exporter) {
        super(exporter);
        recipeTypeTable =
                ArrayTable.create(
                        Arrays.asList(RecipeMap.values()), Arrays.asList(Voltage.values()));
    }

    public void initialize() {
        ItemFactory itemFactory = new ItemFactory(exporter);
        RecipeTypeFactory recipeTypeFactory = new RecipeTypeFactory(exporter);

        for (RecipeMap recipeMap : RecipeMap.values()) {
            Item icon = itemFactory.getItem(recipeMap.getIcon().get(1L));
            for (Voltage voltage : Voltage.values()) {
                recipeTypeTable.put(
                        recipeMap, voltage,
                        recipeTypeFactory.newBuilder()
                                .setId(RECIPE_ID, recipeMap.getShortName(), voltage.getName())
                                .setCategory(RECIPE_CATEGORY)
                                .setType(recipeMap.getName(voltage))
                                .setIcon(icon)
                                .setIconInfo(voltage.getName())
                                .setShapeless(recipeMap.isShapeless())
                                .setItemInputDimension(recipeMap.getItemInputDimension())
                                .setFluidInputDimension(recipeMap.getFluidInputDimension())
                                .setItemOutputDimension(recipeMap.getItemOutputDimension())
                                .setFluidOutputDimension(recipeMap.getFluidOutputDimension())
                                .build());
            }
        }
    }

    public RecipeType getRecipeType(RecipeMap recipeMap, Voltage voltage) {
        return recipeTypeTable.get(recipeMap, voltage);
    }
}
