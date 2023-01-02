package com.github.dcysteine.nesql.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface Repository<R extends Identifiable<K>, K extends Comparable<K>>
        extends JpaRepository<R, K>, JpaSpecificationExecutor<R> {
}
