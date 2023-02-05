package com.github.dcysteine.nesql.exporter.plugin.forge.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemGroupFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.forge.OreDictionary;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Optional;

public class OreDictionaryFactory extends EntityFactory<OreDictionary, String> {
    private final ItemGroupFactory itemGroupFactory;

    public OreDictionaryFactory(PluginExporter exporter) {
        super(exporter);
        itemGroupFactory = new ItemGroupFactory(exporter);
    }

    public OreDictionary get(String name, Collection<ItemStack> itemStacks) {
        String id = IdPrefixUtil.ORE_DICTIONARY.applyPrefix(name);
        ItemGroup itemGroup = itemGroupFactory.get(itemStacks, Optional.of(1), true);

        OreDictionary oreDictionary = new OreDictionary(id, name, itemGroup);
        return findOrPersist(OreDictionary.class, oreDictionary);
    }
}
