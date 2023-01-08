package com.github.dcysteine.nesql.exporter.plugin.base;

import com.github.dcysteine.nesql.exporter.plugin.Plugin;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.RecipeTypeFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.processor.CraftingRecipeProcessor;
import com.github.dcysteine.nesql.exporter.plugin.base.processor.ForgeFluidsProcessor;
import com.github.dcysteine.nesql.exporter.plugin.base.processor.FurnaceRecipeProcessor;
import com.github.dcysteine.nesql.exporter.plugin.base.processor.NeiItemListProcessor;
import com.github.dcysteine.nesql.exporter.util.ItemUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.recipe.Dimension;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import jakarta.persistence.EntityManager;
import net.minecraft.init.Blocks;

import java.util.EnumMap;

/** Base plugin which handles vanilla Minecraft as well as Forge recipes. */
public class BasePlugin implements Plugin {
    public static final String NAME = "base";
    public static final String RECIPE_CATEGORY = "Minecraft";

    private final EntityManager entityManager;
    private final EnumMap<BaseRecipeType, RecipeType> recipeTypeMap;

    public BasePlugin(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.recipeTypeMap = new EnumMap<>(BaseRecipeType.class);
    }

    public RecipeType getRecipeType(BaseRecipeType recipeType) {
        return recipeTypeMap.get(recipeType);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void initialize() {
        RecipeTypeFactory recipeTypeFactory = new RecipeTypeFactory(entityManager);
        ItemFactory itemFactory = new ItemFactory(entityManager);

        Item craftingTable = itemFactory.getItem(ItemUtil.getItemStack(Blocks.crafting_table));
        recipeTypeMap.put(
                BaseRecipeType.SHAPED_CRAFTING,
                recipeTypeFactory.getRecipeType(
                        RECIPE_CATEGORY, "Crafting (Shaped)", craftingTable, false,
                        new Dimension(3, 3), new Dimension(0, 0),
                        new Dimension(1, 1), new Dimension(0, 0)));
        recipeTypeMap.put(
                BaseRecipeType.SHAPELESS_CRAFTING,
                recipeTypeFactory.getRecipeType(
                        RECIPE_CATEGORY, "Crafting (Shapeless)", craftingTable, true,
                        new Dimension(3, 3), new Dimension(0, 0),
                        new Dimension(1, 1), new Dimension(0, 0)));

        Item furnace = itemFactory.getItem(ItemUtil.getItemStack(Blocks.furnace));
        recipeTypeMap.put(
                BaseRecipeType.FURNACE,
                recipeTypeFactory.getRecipeType(
                        RECIPE_CATEGORY, "Furnace", furnace, true,
                        new Dimension(1, 1), new Dimension(0, 0),
                        new Dimension(1, 1), new Dimension(0, 0)));
    }

    @Override
    public void process() {
        new NeiItemListProcessor(entityManager).process();
        new ForgeFluidsProcessor(entityManager).process();
        new CraftingRecipeProcessor(this, entityManager).process();
        new FurnaceRecipeProcessor(this, entityManager).process();
    }
}
