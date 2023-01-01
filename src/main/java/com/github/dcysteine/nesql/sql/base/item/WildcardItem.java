package com.github.dcysteine.nesql.sql.base.item;

import com.github.dcysteine.nesql.sql.Identifiable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * Represents a wildcard recipe input.
 *
 * <p>A wildcard is represented in recipe data by an item stack with item damage set to 32767
 * ({@code Short.MAX_VALUE}, or {@code OreDictionary.WILDCARD_VALUE}).
 * This special value means that item damage should be ignored when checking recipe inputs.
 */
@Entity
@EqualsAndHashCode
@ToString
public class WildcardItem implements Identifiable<Integer> {
    /** The item ID that this represents a wildcard for. */
    @Id
    private int itemId;

    @ManyToMany(mappedBy = "wildcardItems")
    private List<ItemGroup> itemGroups;

    /** Needed by Hibernate. */
    protected WildcardItem() {}

    public WildcardItem(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public Integer getId() {
        return itemId;
    }

    public List<ItemGroup> getItemGroups() {
        return itemGroups;
    }
}
