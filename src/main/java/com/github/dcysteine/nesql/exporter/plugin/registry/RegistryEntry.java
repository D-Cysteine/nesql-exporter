package com.github.dcysteine.nesql.exporter.plugin.registry;

import com.github.dcysteine.nesql.exporter.plugin.Plugin;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import jakarta.persistence.EntityManager;

import java.util.function.Function;

@AutoValue
abstract class RegistryEntry {
    public static RegistryEntry create(
            Function<EntityManager, Plugin> constructor, ModDependency... hardDependencies) {
        return new AutoValue_RegistryEntry(constructor, ImmutableSet.copyOf(hardDependencies));
    }

    public abstract Function<EntityManager, Plugin> getConstructor();
    public abstract ImmutableSet<ModDependency> getHardDependencies();

    public Plugin instantiate(EntityManager entityManager) {
        return getConstructor().apply(entityManager);
    }

    public boolean areDependenciesSatisfied() {
        return getHardDependencies().stream().allMatch(ModDependency::isLoaded);
    }
}
