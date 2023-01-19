package com.github.dcysteine.nesql.exporter.plugin.registry;

import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.sql.Plugin;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import jakarta.persistence.EntityManager;

import java.util.function.Function;

@AutoValue
abstract class RegistryEntry {
    public static RegistryEntry create(
            Plugin plugin, Function<EntityManager, PluginExporter> constructor,
            ModDependency... hardDependencies) {
        return new AutoValue_RegistryEntry(
                plugin, constructor, ImmutableSet.copyOf(hardDependencies));
    }

    public abstract Plugin getPlugin();
    public abstract Function<EntityManager, PluginExporter> getConstructor();
    public abstract ImmutableSet<ModDependency> getHardDependencies();

    public PluginExporter instantiate(EntityManager entityManager) {
        return getConstructor().apply(entityManager);
    }

    public boolean areDependenciesSatisfied() {
        return getHardDependencies().stream().allMatch(ModDependency::isLoaded);
    }

    public boolean isNotDisabled() {
        return !ConfigOptions.DISABLED_PLUGINS.get().contains(getPlugin().getName());
    }
}
