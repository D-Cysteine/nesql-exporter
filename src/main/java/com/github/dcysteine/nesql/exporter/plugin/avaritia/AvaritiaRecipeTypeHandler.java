package com.github.dcysteine.nesql.exporter.plugin.avaritia;

import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.RecipeTypeFactory;
import com.github.dcysteine.nesql.exporter.util.ItemUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import fox.spiteful.avaritia.blocks.LudicrousBlocks;

import java.util.EnumMap;

public class AvaritiaRecipeTypeHandler extends PluginHelper {
    public static final String RECIPE_ID = "avaritia";
    public static final String RECIPE_CATEGORY = "avaritia";

    public enum AvaritiaRecipeType {
        SHAPED_EXTREME_CRAFTING,
        SHAPELESS_EXTREME_CRAFTING,
        COMPRESSOR,
    }

    private final EnumMap<AvaritiaRecipeType, RecipeType> recipeTypeMap;

    public AvaritiaRecipeTypeHandler(PluginExporter exporter) {
        super(exporter);
        recipeTypeMap = new EnumMap<>(AvaritiaRecipeType.class);
    }

    public void initialize() {
        ItemFactory itemFactory = new ItemFactory(exporter);
        RecipeTypeFactory recipeTypeFactory = new RecipeTypeFactory(exporter);

        Item direCraftingTable =
                itemFactory.get(ItemUtil.getItemStack(LudicrousBlocks.dire_crafting).get());
        recipeTypeMap.put(
                AvaritiaRecipeType.SHAPED_EXTREME_CRAFTING,
                recipeTypeFactory.newBuilder()
                        .setId(RECIPE_ID, "extremecrafting", "shaped")
                        .setCategory(RECIPE_CATEGORY)
                        .setType("Extreme Crafting (Shaped)")
                        .setIcon(direCraftingTable)
                        .setShapeless(false)
                        .setItemInputDimension(9, 9)
                        .setItemOutputDimension(1, 1)
                        .build());
        recipeTypeMap.put(
                AvaritiaRecipeType.SHAPELESS_EXTREME_CRAFTING,
                recipeTypeFactory.newBuilder()
                        .setId(RECIPE_ID, "extremecrafting", "shapeless")
                        .setCategory(RECIPE_CATEGORY)
                        .setType("Extreme Crafting (Shapeless)")
                        .setIcon(direCraftingTable)
                        .setShapeless(true)
                        .setItemInputDimension(9, 9)
                        .setItemOutputDimension(1, 1)
                        .build());

        Item compressor = itemFactory.get(ItemUtil.getItemStack(LudicrousBlocks.compressor).get());
        recipeTypeMap.put(
                AvaritiaRecipeType.COMPRESSOR,
                recipeTypeFactory.newBuilder()
                        .setId(RECIPE_ID, "compressor")
                        .setCategory(RECIPE_CATEGORY)
                        .setType("Compressor")
                        .setIcon(compressor)
                        .setShapeless(true)
                        .setItemInputDimension(1, 1)
                        .setItemOutputDimension(1, 1)
                        .build());
    }

    public RecipeType getRecipeType(AvaritiaRecipeType recipeType) {
        return recipeTypeMap.get(recipeType);
    }
}
