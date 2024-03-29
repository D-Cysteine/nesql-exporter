package com.github.dcysteine.nesql.exporter.plugin;

import com.github.dcysteine.nesql.sql.Identifiable;

import java.util.Optional;

/**
 * Abstract class for entity factories.
 *
 * <p>When constructing a new entity, always return the result of calling
 * {@link #findOrPersist(Class, Identifiable)} on it, as we must reference the already-persisted
 * instance, if it exists.
 */
public abstract class EntityFactory<T extends Identifiable<K>, K extends Comparable<K>>
        extends PluginHelper {
    protected EntityFactory(PluginExporter exporter) {
        super(exporter);
    }

    /**
     * Finds a persisted entity, or returns empty optional if no entity is persisted.
     */
    public Optional<T> find(Class<T> clazz, K id) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    /**
     * Returns the already persisted instance, or if not already persisted, persists the provided
     * instance.
     */
    public T findOrPersist(Class<T> clazz, T entity) {
        T persisted = entityManager.find(clazz, entity.getId());
        if (persisted != null) {
            return persisted;
        }

        entityManager.persist(entity);
        return entity;
    }
}
