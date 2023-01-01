package com.github.dcysteine.nesql.exporter.plugin.base;

import com.github.dcysteine.nesql.exporter.plugin.Plugin;
import com.github.dcysteine.nesql.exporter.plugin.base.processor.CraftingRecipeProcessor;
import com.github.dcysteine.nesql.exporter.plugin.base.processor.FurnaceRecipeProcessor;
import com.github.dcysteine.nesql.exporter.plugin.base.processor.NeiItemListProcessor;
import jakarta.persistence.EntityManager;

/** Base plugin which handles vanilla Minecraft as well as Forge recipes. */
public class BasePlugin implements Plugin {
    private final EntityManager entityManager;

    public BasePlugin(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void process() {
        new NeiItemListProcessor(entityManager).process();
        new CraftingRecipeProcessor(entityManager).process();
        new FurnaceRecipeProcessor(entityManager).process();
    }
}
