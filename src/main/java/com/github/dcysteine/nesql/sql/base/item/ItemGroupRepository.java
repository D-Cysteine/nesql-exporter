package com.github.dcysteine.nesql.sql.base.item;

import com.github.dcysteine.nesql.sql.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemGroupRepository extends Repository<ItemGroup, String> {
    @Query(value = "SELECT * FROM ITEM_GROUP WHERE ID IN ("
            + "SELECT ITEM_GROUP_ID FROM ITEM_GROUP_ITEM_STACKS WHERE ITEM_STACKS_ITEM_ID = ?1)",
            nativeQuery = true)
    List<ItemGroup> findByItem(String itemId);

    @Query(value = "SELECT * FROM ITEM_GROUP WHERE ID IN ("
            + "SELECT ITEM_GROUP_ID FROM ITEM_GROUP_WILDCARD_ITEM_STACKS"
            + " WHERE WILDCARD_ITEM_STACKS_ITEM_ID = ?1)",
            nativeQuery = true)
    List<ItemGroup> findByWildcardItemId(int minecraftItemId);
}
