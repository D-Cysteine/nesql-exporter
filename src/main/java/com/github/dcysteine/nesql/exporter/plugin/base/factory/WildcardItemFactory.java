package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.sql.base.item.WildcardItem;
import jakarta.persistence.EntityManager;

public class WildcardItemFactory extends EntityFactory<WildcardItem, Integer> {
    public WildcardItemFactory(EntityManager entityManager) {
        super(entityManager);
    }

    public WildcardItem getWildcardItem(int itemId) {
        WildcardItem wildcardItem = new WildcardItem(itemId);
        return findOrPersist(WildcardItem.class, wildcardItem);
    }
}
