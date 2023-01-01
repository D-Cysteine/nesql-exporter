package com.github.dcysteine.nesql.sql.base.item;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.Sql;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SortNatural;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

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

    /** Needed by Hibernate. */
    protected WildcardItem() {}

    public WildcardItem(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public Integer getId() {
        return itemId;
    }
}
