package com.github.dcysteine.nesql.sql.repository;

import com.github.dcysteine.nesql.sql.data.SqlItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<SqlItem, String> {
}
