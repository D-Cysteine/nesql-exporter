package com.github.dcysteine.nesql.exporter.plugin.thaumcraft.factory;

import com.djgiannuzz.thaumcraftneiplugin.ModItems;
import com.djgiannuzz.thaumcraftneiplugin.items.ItemAspect;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.thaumcraft.Aspect;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.AspectList;

import java.util.Arrays;

public class AspectFactory extends EntityFactory<Aspect, String> {
    private final ItemFactory itemFactory;

    public AspectFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
    }

    public Aspect get(thaumcraft.api.aspects.Aspect aspect) {
        String id = IdPrefixUtil.ASPECT.applyPrefix(aspect.getName());
        Aspect aspectEntity = entityManager.find(Aspect.class, id);
        if (aspectEntity != null) {
            return aspectEntity;
        }

        ItemStack iconItemStack = new ItemStack(ModItems.itemAspect, 1, 0);
        ItemAspect.setAspects(iconItemStack, new AspectList().add(aspect, 2));
        Item icon = itemFactory.get(iconItemStack);

        aspectEntity =
                new Aspect(
                        id, icon,
                        aspect.getName(), aspect.getLocalizedDescription(), aspect.isPrimal());
        entityManager.persist(aspectEntity);
        return aspectEntity;
    }

    public Aspect findAspect(thaumcraft.api.aspects.Aspect aspect) {
        String id = IdPrefixUtil.ASPECT.applyPrefix(aspect.getName());
        Aspect aspectEntity = entityManager.find(Aspect.class, id);
        if (aspectEntity == null) {
            throw new IllegalStateException("Could not find aspect: " + aspect.getName());
        }
        return aspectEntity;
    }

    public void setComponents(thaumcraft.api.aspects.Aspect aspect) {
        thaumcraft.api.aspects.Aspect[] components = aspect.getComponents();
        if (components == null) {
            return;
        }

        Aspect aspectEntity = findAspect(aspect);
        Arrays.stream(aspect.getComponents())
                .map(this::findAspect)
                .forEach(aspectEntity::addComponent);
    }
}
