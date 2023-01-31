package com.github.dcysteine.nesql.exporter.plugin.base;

import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.RecipeTypeFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.processor.CraftingRecipeProcessor;
import com.github.dcysteine.nesql.exporter.plugin.base.processor.ForgeFluidsProcessor;
import com.github.dcysteine.nesql.exporter.plugin.base.processor.FurnaceRecipeProcessor;
import com.github.dcysteine.nesql.exporter.util.ItemUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import jakarta.persistence.EntityManager;
import net.minecraft.init.Blocks;

import java.util.EnumMap;

/** Base plugin which handles vanilla Minecraft as well as Forge recipes. */
public class BasePluginExporter implements PluginExporter {
    public static final String RECIPE_CATEGORY = "Minecraft";

    private final EntityManager entityManager;
    private final EnumMap<BaseRecipeType, RecipeType> recipeTypeMap;

    public BasePluginExporter(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.recipeTypeMap = new EnumMap<>(BaseRecipeType.class);
    }

    public RecipeType getRecipeType(BaseRecipeType recipeType) {
        return recipeTypeMap.get(recipeType);
    }

    @Override
    public void initialize() {
        RecipeTypeFactory recipeTypeFactory = new RecipeTypeFactory(entityManager);
        ItemFactory itemFactory = new ItemFactory(entityManager);

        Item craftingTable = itemFactory.getItem(ItemUtil.getItemStack(Blocks.crafting_table));
        recipeTypeMap.put(
                BaseRecipeType.SHAPED_CRAFTING,
                recipeTypeFactory.newBuilder()
                        .setId("base", "crafting", "shaped")
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
                        .setId("base", "crafting", "shapeless")
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
                        .setId("base", "furnace")
                        .setCategory(RECIPE_CATEGORY)
                        .setType("Furnace")
                        .setIcon(furnace)
                        .setShapeless(true)
                        .setItemInputDimension(1, 1)
                        .setItemOutputDimension(1, 1)
                        .build());
    }

    @Override
    public void process() {
        new ForgeFluidsProcessor(entityManager).process();
        new CraftingRecipeProcessor(this, entityManager).process();
        new FurnaceRecipeProcessor(this, entityManager).process();
    }
}
