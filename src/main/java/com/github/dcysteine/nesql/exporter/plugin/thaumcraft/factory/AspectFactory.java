package com.github.dcysteine.nesql.exporter.plugin.thaumcraft.factory;

import com.djgiannuzz.thaumcraftneiplugin.ModItems;
import com.djgiannuzz.thaumcraftneiplugin.items.ItemAspect;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.thaumcraft.Aspect;
import jakarta.persistence.EntityManager;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.AspectList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AspectFactory extends EntityFactory<Aspect, String> {
    private final ItemFactory itemFactory;

    public AspectFactory(EntityManager entityManager) {
        super(entityManager);
        itemFactory = new ItemFactory(entityManager);
    }

    public Aspect getAspect(thaumcraft.api.aspects.Aspect aspect) {
        String id = IdPrefixUtil.ASPECT.applyPrefix(aspect.getName());

        ItemStack iconItemStack = new ItemStack(ModItems.itemAspect, 1, 0);
        ItemAspect.setAspects(iconItemStack, new AspectList().add(aspect, 2));
        Item icon = itemFactory.getItem(iconItemStack);

        Aspect aspectEntity =
                new Aspect(
                        id, icon,
                        aspect.getName(), aspect.getLocalizedDescription(), aspect.isPrimal());
        return findOrPersist(Aspect.class, aspectEntity);
    }

    public Aspect findAspect(thaumcraft.api.aspects.Aspect aspect) {
        String id = IdPrefixUtil.ASPECT.applyPrefix(aspect.getName());
        Optional<Aspect> aspectEntity = find(Aspect.class, id);
        if (!aspectEntity.isPresent()) {
            throw new IllegalStateException("Could not find aspect: " + aspect.getName());
        }
        return aspectEntity.get();
    }

    public void setComponents(thaumcraft.api.aspects.Aspect aspect) {
        Set<Aspect> components =
                Arrays.stream(aspect.getComponents())
                        .map(this::findAspect)
                        .collect(Collectors.toCollection(HashSet::new));

        findAspect(aspect).setComponents(components);
    }
}
