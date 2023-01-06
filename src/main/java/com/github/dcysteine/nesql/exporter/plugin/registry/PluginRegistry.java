package com.github.dcysteine.nesql.exporter.plugin.registry;

import com.github.dcysteine.nesql.exporter.plugin.Plugin;
import com.github.dcysteine.nesql.exporter.plugin.base.BasePlugin;
import com.google.common.collect.ImmutableList;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;


/** Registry of plugins. Register new plugins here! */
public class PluginRegistry {
    private static final ImmutableList<RegistryEntry> entries;

    static {
        ImmutableList.Builder<RegistryEntry> builder = ImmutableList.builder();

        // Add new plugins here!
        builder.add(RegistryEntry.create(BasePlugin::new));

        entries = builder.build();
    }

    /** Map of plugin name to plugin. */
    private List<Plugin> activePlugins = new ArrayList<>();

    /** Constructs plugins whose dependencies are met. Returns a list of activated plugins. */
    public List<Plugin> initialize(EntityManager entityManager) {
        entries.stream()
                .filter(RegistryEntry::areDependenciesSatisfied)
                .map(entry -> entry.instantiate(entityManager))
                .forEach(activePlugins::add);

        return activePlugins;
    }

    public void initializePlugins() {
        activePlugins.forEach(Plugin::initialize);
    }

    public void processPlugins() {
        activePlugins.forEach(Plugin::process);
    }

    public void postProcessPlugins() {
        activePlugins.forEach(Plugin::postProcess);
    }
}
