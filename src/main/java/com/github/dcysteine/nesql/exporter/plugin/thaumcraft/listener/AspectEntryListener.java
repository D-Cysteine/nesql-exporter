package com.github.dcysteine.nesql.exporter.plugin.thaumcraft.listener;

import com.github.dcysteine.nesql.exporter.plugin.Database;
import com.github.dcysteine.nesql.exporter.plugin.thaumcraft.factory.AspectEntryFactory;
import com.github.dcysteine.nesql.exporter.plugin.thaumcraft.factory.AspectFactory;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.thaumcraft.Aspect;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

public class AspectEntryListener implements Database.ItemListener {
    AspectFactory aspectFactory;
    AspectEntryFactory aspectEntryFactory;

    public AspectEntryListener(Database database) {
        aspectFactory = new AspectFactory(database);
        aspectEntryFactory = new AspectEntryFactory(database);
    }

    @Override
    public void accept(Item item, ItemStack itemStack) {
        AspectList aspects = ThaumcraftCraftingManager.getObjectTags(itemStack);
        aspects = ThaumcraftCraftingManager.getBonusTags(itemStack, aspects);
        for (thaumcraft.api.aspects.Aspect aspect : aspects.getAspects()) {
            Aspect aspectEntity = aspectFactory.getAspect(aspect);
            aspectEntryFactory.getAspectEntry(aspectEntity, item, aspects.getAmount(aspect));
        }
    }
}
