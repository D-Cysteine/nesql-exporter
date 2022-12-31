package com.github.dcysteine.nesql.exporter.plugin;

import com.github.dcysteine.nesql.sql.Identifiable;
import jakarta.persistence.EntityManager;

import javax.annotation.Nullable;

/**
 * Abstract class for entity factories.
 *
 * <p>When constructing a new entity, always return the result of calling
 * {@link #findOrPersist(Class, Identifiable)} (Class, Identifiable)} on it, as we must reference
 * the already-persisted instance, if it exists.
 */
public abstract class EntityFactory<T extends Identifiable<K>, K extends Comparable<K>> {
    protected final EntityManager entityManager;

    protected EntityFactory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Finds a persisted entity, or returns null if no entity is persisted.
     *
     * @return null if {@code entity} was not found.
     */
    @Nullable
    public T find(Class<T> clazz, K id) {
        return entityManager.find(clazz, id);
    }

    /**
     * Returns the already persisted instance, or if not already persisted, persists the provided
     * instance.
     */
    public T findOrPersist(Class<T> clazz, T entity) {
        T persisted = find(clazz, entity.getId());
        if (persisted != null) {
            return persisted;
        }

        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        return entity;
    }
}
