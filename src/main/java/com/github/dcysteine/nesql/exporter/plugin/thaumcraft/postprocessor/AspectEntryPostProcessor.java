package com.github.dcysteine.nesql.exporter.plugin.thaumcraft.postprocessor;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.thaumcraft.factory.AspectEntryFactory;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.EntityManager;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

import java.util.List;

public class AspectEntryPostProcessor {
    private final EntityManager entityManager;

    public AspectEntryPostProcessor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // TODO we need to register this method as a listener on ItemFactory persist
    // because we cannot go from Item -> ItemStack due to no way to reverse NBT.toString()
    public void postProcess() {
        List<Item> items =
                entityManager.createQuery("Select * from Item", Item.class).getResultList();
        int total = items.size();
        Logger.THAUMCRAFT.info("Post-processing {} items...", total);

        AspectEntryFactory aspectEntryFactory = new AspectEntryFactory(entityManager);
        int count = 0;
        for (Item item : items) {
            count++;

            ItemStack itemStack =
                    new ItemStack(
                            net.minecraft.item.Item.getItemById(item.getItemId()),
                            1, item.getItemDamage());

            AspectList aspects = ThaumcraftCraftingManager.getObjectTags(itemStack);
            aspects = ThaumcraftCraftingManager.getBonusTags(itemStack, aspects);
            for (Aspect aspect : aspects.getAspects()) {
                aspectEntryFactory.getAspectEntry(aspect, itemStack, aspects.getAmount(aspect));
            }

            if (Logger.intermittentLog(count)) {
                Logger.THAUMCRAFT.info("Post-processed item {} of {}", count, total);
                Logger.THAUMCRAFT.info("Most recent item: {}", item.getLocalizedName());
            }
        }

        Logger.THAUMCRAFT.info("Finished post-processing items!");
    }
}
