package com.github.dcysteine.nesql.exporter.plugin.thaumcraft.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.thaumcraft.Aspect;
import com.github.dcysteine.nesql.sql.thaumcraft.AspectEntry;
import jakarta.persistence.EntityManager;
import net.minecraft.item.ItemStack;

public class AspectEntryFactory extends EntityFactory<AspectEntry, String> {
    private final AspectFactory aspectFactory;
    private final ItemFactory itemFactory;

    public AspectEntryFactory(EntityManager entityManager) {
        super(entityManager);
        aspectFactory = new AspectFactory(entityManager);
        itemFactory = new ItemFactory(entityManager);
    }

    public AspectEntry getAspectEntry(
            thaumcraft.api.aspects.Aspect aspect, ItemStack itemStack, int quantity) {
        Aspect aspectEntity = aspectFactory.getAspect(aspect);
        Item item = itemFactory.getItem(itemStack);
        String id =
                IdPrefixUtil.ASPECT_ENTRY.applyPrefix(
                        aspectEntity.getId() + IdUtil.ID_SEPARATOR + item.getId());

        AspectEntry aspectEntry = new AspectEntry(id, aspectEntity, item, quantity);
        return findOrPersist(AspectEntry.class, aspectEntry);
    }
}
