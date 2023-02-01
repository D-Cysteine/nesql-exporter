package com.github.dcysteine.nesql.exporter.plugin.thaumcraft.factory;

import com.github.dcysteine.nesql.exporter.plugin.Database;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.thaumcraft.Aspect;
import com.github.dcysteine.nesql.sql.thaumcraft.AspectEntry;

public class AspectEntryFactory extends EntityFactory<AspectEntry, String> {
    public AspectEntryFactory(Database database) {
        super(database);
    }

    public AspectEntry getAspectEntry(Aspect aspect, Item item, int amount) {
        String id =
                IdPrefixUtil.ASPECT_ENTRY.applyPrefix(
                        aspect.getId() + IdUtil.ID_SEPARATOR + item.getId());

        AspectEntry aspectEntry = new AspectEntry(id, aspect, item, amount);
        return findOrPersist(AspectEntry.class, aspectEntry);
    }
}
