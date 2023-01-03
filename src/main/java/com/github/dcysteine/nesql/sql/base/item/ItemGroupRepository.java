package com.github.dcysteine.nesql.sql.base.item;

import com.github.dcysteine.nesql.sql.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemGroupRepository extends Repository<ItemGroup, String> {
    @Query(value = "SELECT * FROM ITEMGROUP WHERE ID IN ("
            + "SELECT ITEMGROUP_ID FROM ITEMGROUP_ITEMSTACKS WHERE ITEM_ID = ?1)",
            nativeQuery = true)
    List<ItemGroup> findByItem(String itemId);

    @Query(value = "SELECT * FROM ITEMGROUP WHERE ID IN ("
            + "SELECT ITEMGROUP_ID FROM ITEMGROUP_WILDCARDITEMSTACKS WHERE ITEMID = ?1)",
            nativeQuery = true)
    List<ItemGroup> findByWildcardItemId(int minecraftItemId);
}
