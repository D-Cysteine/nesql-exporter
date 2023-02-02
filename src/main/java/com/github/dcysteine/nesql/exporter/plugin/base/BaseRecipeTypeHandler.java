package com.github.dcysteine.nesql.exporter.plugin.base;

import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.RecipeTypeFactory;
import com.github.dcysteine.nesql.exporter.util.ItemUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import net.minecraft.init.Blocks;

import java.util.EnumMap;

public class BaseRecipeTypeHandler extends PluginHelper {
    public static final String RECIPE_ID = "base";
    public static final String RECIPE_CATEGORY = "minecraft";

    public enum BaseRecipeType {
        SHAPED_CRAFTING,
        SHAPELESS_CRAFTING,
        FURNACE,
        ;
    }

    private final EnumMap<BaseRecipeType, RecipeType> recipeTypeMap;

    public BaseRecipeTypeHandler(PluginExporter exporter) {
        super(exporter);
        recipeTypeMap = new EnumMap<>(BaseRecipeType.class);
    }

    public void initialize() {
        ItemFactory itemFactory = new ItemFactory(exporter);
        RecipeTypeFactory recipeTypeFactory = new RecipeTypeFactory(exporter);

        Item craftingTable = itemFactory.getItem(ItemUtil.getItemStack(Blocks.crafting_table));
        recipeTypeMap.put(
                BaseRecipeType.SHAPED_CRAFTING,
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
                BaseRecipeType.SHAPELESS_CRAFTING,
                recipeTypeFactory.newBuilder()
                        .setId(RECIPE_ID, "crafting", "shapeless")
                        .setCategory(RECIPE_CATEGORY)
                        .setType("Crafting (Shapeless)")
                        .setIcon(craftingTable)
                        .setShapeless(true)
                        .setItemInputDimension(3, 3)
                        .setItemOutputDimension(1, 1)
                        .build());

        Item furnace = itemFactory.getItem(ItemUtil.getItemStack(Blocks.furnace));
        recipeTypeMap.put(
                BaseRecipeType.FURNACE,
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

    public RecipeType getRecipeType(BaseRecipeType recipeType) {
        return recipeTypeMap.get(recipeType);
    }
}
