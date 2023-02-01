package com.github.dcysteine.nesql.exporter.plugin.registry;

import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.BasePluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.nei.NeiPluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.quest.QuestPluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.thaumcraft.ThaumcraftPluginExporter;
import com.github.dcysteine.nesql.sql.Plugin;
import com.google.common.collect.ImmutableList;
import jakarta.persistence.EntityManager;

import java.util.EnumMap;
import java.util.Map;


/** Registry of plugins. Register new plugins here! */
public class PluginRegistry {
    private static final ImmutableList<RegistryEntry> entries;

    static {
        ImmutableList.Builder<RegistryEntry> builder = ImmutableList.builder();

        // Add new plugins here!
        builder.add(RegistryEntry.create(Plugin.BASE, BasePluginExporter::new));
        builder.add(RegistryEntry.create(Plugin.NEI, NeiPluginExporter::new));

        builder.add(
                RegistryEntry.create(
                        Plugin.THAUMCRAFT, ThaumcraftPluginExporter::new,
                        ModDependency.THAUMCRAFT, ModDependency.THAUMCRAFT_NEI));
        builder.add(
                RegistryEntry.create(
                        Plugin.QUEST, QuestPluginExporter::new, ModDependency.BETTER_QUESTING));

        entries = builder.build();
    }

    private final Map<Plugin, PluginExporter> activePlugins = new EnumMap<>(Plugin.class);

    /** Constructs plugins whose dependencies are met. Returns a list of activated plugins. */
    public Map<Plugin, PluginExporter> initialize(EntityManager entityManager) {
        entries.stream()
                .filter(RegistryEntry::areDependenciesSatisfied)
                .filter(RegistryEntry::isNotDisabled)
                .forEach(
                        entry ->
                                activePlugins.put(
                                        entry.getPlugin(), entry.instantiate(entityManager)));

        return activePlugins;
    }

    public Map<Plugin, PluginExporter> getActivePlugins() {
        return activePlugins;
    }

    public void initializePlugins() {
        activePlugins.values().forEach(PluginExporter::initialize);
    }

    public void processPlugins() {
        activePlugins.values().forEach(PluginExporter::process);
    }

    public void postProcessPlugins() {
        activePlugins.values().forEach(PluginExporter::postProcess);
    }
}
