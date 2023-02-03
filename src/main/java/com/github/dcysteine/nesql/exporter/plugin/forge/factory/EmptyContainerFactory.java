package com.github.dcysteine.nesql.exporter.plugin.forge.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.forge.EmptyContainer;

import java.util.Map;

public class EmptyContainerFactory extends EntityFactory<EmptyContainer, String> {
    public EmptyContainerFactory(PluginExporter exporter) {
        super(exporter);
    }

    public EmptyContainer get(Item emptyContainer, Map<Item, Integer> containers) {
        String id = IdPrefixUtil.FLUID_CONTAINER.applyPrefix(emptyContainer.getId());
        EmptyContainer emptyContainerEntity = new EmptyContainer(id, emptyContainer, containers);
        return findOrPersist(EmptyContainer.class, emptyContainerEntity);
    }
}
