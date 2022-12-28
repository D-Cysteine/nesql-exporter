package com.github.dcysteine.nesql.exporter.handler;

import org.hibernate.Session;

import javax.persistence.EntityManager;

// TODO delete this directory
public abstract class AbstractSaver {
    private final Session session;

    protected AbstractSaver(EntityManager entityManager) {
        session = entityManager.unwrap(Session.class);
    }

    /**
     * Wrapper for {@link #saveImpl()} which wraps the whole thing in a session.
     *
     * <p>This is necessary to get Hibernate to actually commit the changes.
     */
    public final void save() {
        session.beginTransaction();
        saveImpl();
        session.getTransaction().commit();
    }

    /** Override this method to save things to Spring repositories. */
    protected abstract void saveImpl();
}
