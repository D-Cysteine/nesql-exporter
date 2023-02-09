package com.github.dcysteine.nesql.exporter.plugin.minecraft;

import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.RecipeTypeFactory;
import com.github.dcysteine.nesql.exporter.util.ItemUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import net.minecraft.init.Blocks;

import java.util.EnumMap;

public class MinecraftRecipeTypeHandler extends PluginHelper {
    public static final String RECIPE_ID = "minecraft";
    public static final String RECIPE_CATEGORY = "minecraft";

    public enum MinecraftRecipeType {
        SHAPED_CRAFTING,
        SHAPELESS_CRAFTING,
        FURNACE,
        ;
    }

    private final EnumMap<MinecraftRecipeType, RecipeType> recipeTypeMap;

    public MinecraftRecipeTypeHandler(PluginExporter exporter) {
        super(exporter);
        recipeTypeMap = new EnumMap<>(MinecraftRecipeType.class);
    }

    public void initialize() {
        ItemFactory itemFactory = new ItemFactory(exporter);
        RecipeTypeFactory recipeTypeFactory = new RecipeTypeFactory(exporter);

        Item craftingTable = itemFactory.get(ItemUtil.getItemStack(Blocks.crafting_table).get());
        recipeTypeMap.put(
                MinecraftRecipeType.SHAPED_CRAFTING,
                recipeTypeFactory.newBuilder()
                        .setId(RECIPE_ID, "crafting", "shaped")
                        .setCategory(RECIPE_CATEGORY)
                        .setType("Crafting (Shaped)")
                        .setIcon(craftingTable)
                        .setShapeless(false)
                        .setItemInputDimension(3, 3)
                        .setItemOutputDimension(1, 1)
                        .build());
        recipeTypeMap.put(
                MinecraftRecipeType.SHAPELESS_CRAFTING,
                recipeTypeFactory.newBuilder()
                        .setId(RECIPE_ID, "crafting", "shapeless")
                        .setCategory(RECIPE_CATEGORY)
                        .setType("Crafting (Shapeless)")
                        .setIcon(craftingTable)
                        .setShapeless(true)
                        .setItemInputDimension(3, 3)
                        .setItemOutputDimension(1, 1)
                        .build());

        Item furnace = itemFactory.get(ItemUtil.getItemStack(Blocks.furnace).get());
        recipeTypeMap.put(
                MinecraftRecipeType.FURNACE,
                recipeTypeFactory.newBuilder()
                        .setId(RECIPE_ID, "furnace")
                        .setCategory(RECIPE_CATEGORY)
                        .setType("Furnace")
                        .setIcon(furnace)
                        .setShapeless(true)
                        .setItemInputDimension(1, 1)
                        .setItemOutputDimension(1, 1)
                        .build());
    }

    public RecipeType getRecipeType(MinecraftRecipeType recipeType) {
        return recipeTypeMap.get(recipeType);
    }
}
