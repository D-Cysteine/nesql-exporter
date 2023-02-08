package com.github.dcysteine.nesql.exporter.registry;

import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.sql.Plugin;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;

import java.util.function.BiFunction;

@AutoValue
abstract class RegistryEntry {
    public static RegistryEntry create(
            Plugin plugin, BiFunction<Plugin, ExporterState, PluginExporter> constructor,
            ModDependency... hardDependencies) {
        return new AutoValue_RegistryEntry(
                plugin, constructor, ImmutableSet.copyOf(hardDependencies));
    }

    public abstract Plugin getPlugin();
    public abstract BiFunction<Plugin, ExporterState, PluginExporter> getConstructor();
    public abstract ImmutableSet<ModDependency> getHardDependencies();

    public PluginExporter instantiate(ExporterState exporterState) {
        return getConstructor().apply(getPlugin(), exporterState);
    }

    public boolean areDependenciesSatisfied() {
        return getHardDependencies().stream().allMatch(ModDependency::isLoaded);
    }

    public boolean isEnabled() {
        return ConfigOptions.ENABLED_PLUGINS.get().contains(getPlugin().getName());
    }
}
