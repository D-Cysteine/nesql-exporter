package com.github.dcysteine.nesql.sql.base.item;

import com.github.dcysteine.nesql.sql.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends Repository<Item, String> {
    @Query(value = "SELECT * FROM ITEM WHERE ITEM_ID = ?1", nativeQuery = true)
    List<Item> findByItemId(int minecraftItemId);

    @Query(value = "SELECT * FROM ITEM WHERE ITEM_ID = ?1"
            + " ORDER BY ITEM_DAMAGE ASC, NBT ASC NULLS FIRST", nativeQuery = true)
    List<Item> findBaseItemByItemId(int minecraftItemId);
}
