package com.github.dcysteine.nesql.exporter.plugin.thaumcraft;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.thaumcraft.factory.AspectEntryFactory;
import com.github.dcysteine.nesql.exporter.plugin.thaumcraft.factory.AspectFactory;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.thaumcraft.Aspect;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

public class AspectEntryListener extends PluginHelper implements ExporterState.ItemListener {
    AspectFactory aspectFactory;
    AspectEntryFactory aspectEntryFactory;

    public AspectEntryListener(PluginExporter exporter) {
        super(exporter);
        aspectFactory = new AspectFactory(exporter);
        aspectEntryFactory = new AspectEntryFactory(exporter);
    }

    @Override
    public void accept(Item item, ItemStack itemStack) {
        try {
            AspectList aspects = ThaumcraftCraftingManager.getObjectTags(itemStack);
            aspects = ThaumcraftCraftingManager.getBonusTags(itemStack, aspects);
            if (aspects.size() <= 0) {
                return;
            }

            for (thaumcraft.api.aspects.Aspect aspect : aspects.getAspects()) {
                int amount = aspects.getAmount(aspect);
                if (amount <= 0) {
                    continue;
                }

                Aspect aspectEntity = aspectFactory.getAspect(aspect);
                aspectEntryFactory.getAspectEntry(aspectEntity, item, amount);
            }
        } catch (StackOverflowError e) {
            // Thaumcraft why you do this T_T
            logger.error("Stack overflow computing aspects for: " + item.getLocalizedName(), e);
        }
    }
}
