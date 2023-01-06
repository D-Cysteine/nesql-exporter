package com.github.dcysteine.nesql.exporter.plugin.base;

import com.github.dcysteine.nesql.exporter.plugin.Plugin;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.processor.CraftingRecipeProcessor;
import com.github.dcysteine.nesql.exporter.plugin.base.processor.ForgeFluidsProcessor;
import com.github.dcysteine.nesql.exporter.plugin.base.processor.FurnaceRecipeProcessor;
import com.github.dcysteine.nesql.exporter.plugin.base.processor.NeiItemListProcessor;
import com.github.dcysteine.nesql.exporter.util.ItemUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeInfo;
import jakarta.persistence.EntityManager;
import net.minecraft.init.Blocks;

import java.util.EnumMap;

/** Base plugin which handles vanilla Minecraft as well as Forge recipes. */
public class BasePlugin implements Plugin {
    private static final String NAME = "base";

    private final EntityManager entityManager;
    private final EnumMap<RecipeType, RecipeInfo> recipeInfoMap;

    public BasePlugin(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.recipeInfoMap = new EnumMap<>(RecipeType.class);
    }

    public RecipeInfo getRecipeInfo(RecipeType recipeType) {
        return recipeInfoMap.get(recipeType);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void initialize() {
        Item craftingTable =
                new ItemFactory(entityManager).getItem(
                        ItemUtil.getItemStack(Blocks.crafting_table));
        recipeInfoMap.put(
                RecipeType.SHAPED_CRAFTING,
                new RecipeInfo(NAME, "shaped-crafting", craftingTable, false));
        recipeInfoMap.put(
                RecipeType.SHAPELESS_CRAFTING,
                new RecipeInfo(NAME, "shapeless-crafting", craftingTable, true));

        Item furnace =
                new ItemFactory(entityManager).getItem(ItemUtil.getItemStack(Blocks.lit_furnace));
        recipeInfoMap.put(
                RecipeType.FURNACE, new RecipeInfo(NAME, "furnace", furnace, true));
    }

    @Override
    public void process() {
        new NeiItemListProcessor(entityManager).process();
        new ForgeFluidsProcessor(entityManager).process();
        new CraftingRecipeProcessor(this, entityManager).process();
        new FurnaceRecipeProcessor(this, entityManager).process();
    }
}
