package com.github.dcysteine.nesql.sql.base.item;

import com.github.dcysteine.nesql.sql.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends Repository<Item, String> {
    @Query(value = "SELECT * FROM ITEM WHERE ITEM_ID = ?1", nativeQuery = true)
    List<Item> findByItemId(int minecraftItemId);

    @Query(value = "SELECT * FROM ITEM WHERE ITEM_ID = ?1"
            + " AND NBT IS NULL ORDER BY ITEM_DAMAGE ASC", nativeQuery = true)
    List<Item> findBaseItemByItemId(int minecraftItemId);
}
