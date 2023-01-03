package com.github.dcysteine.nesql.sql.base.item;

import com.github.dcysteine.nesql.sql.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends Repository<Item, String> {
    @Query(value = "SELECT * FROM ITEM WHERE ITEMID = ?1", nativeQuery = true)
    List<Item> findByItemId(int minecraftItemId);

    @Query(value = "SELECT * FROM ITEM WHERE ITEMID = ?1"
            + " AND NBT IS NULL ORDER BY ITEMDAMAGE ASC", nativeQuery = true)
    List<Item> findBaseItemByItemId(int minecraftItemId);
}
