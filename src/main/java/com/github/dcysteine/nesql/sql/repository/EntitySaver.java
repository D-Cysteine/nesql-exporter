package com.github.dcysteine.nesql.sql.repository;

import org.hibernate.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import javax.persistence.EntityManager;

// TODO make a factory for all of these, that tracks already saved entities and prevents
// re-saving them? Special handling for rendering, and esp. rendering of fluids.
// HashSet of table IDs.
//
// Alternatively, init an object that tracks these in Exporter, and pass it everywhere.
// Object contains SetMultimap<Class<T extends JpaRepository<R, K>>, K> tracking seen keys.
// Also contains / constructs on demand EntitySaver for each table, on use.
// Maybe we can move the logic in this class into that object?
/** Templated class that handles saving rows to tables. */
public class EntitySaver<T extends JpaRepository<R, ?>, R> {
    private final Session session;
    private final T repository;

    public EntitySaver(EntityManager entityManager, Class<T> clazz) {
        session = entityManager.unwrap(Session.class);
        repository = new JpaRepositoryFactory(entityManager).getRepository(clazz);
    }

    public void save(R row) {
        session.beginTransaction();
        repository.save(row);
        session.getTransaction().commit();
    }
}
