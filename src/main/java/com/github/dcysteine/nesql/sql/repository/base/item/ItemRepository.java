package com.github.dcysteine.nesql.sql.repository.base.item;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemRow, String> {
}
