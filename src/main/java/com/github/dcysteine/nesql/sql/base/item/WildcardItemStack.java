package com.github.dcysteine.nesql.sql.base.item;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;

/**
 * Represents a wildcard recipe input.
 *
 * <p>A wildcard is represented in recipe data by an item stack with item damage set to 32767
 * ({@code Short.MAX_VALUE}, or {@code OreDictionary.WILDCARD_VALUE}).
 * This special value means that item damage should be ignored when checking recipe inputs.
 */
@Embeddable
@EqualsAndHashCode
@ToString
public class WildcardItemStack implements Comparable<WildcardItemStack> {
    /** This is the Minecraft item ID, NOT the {@code Item} entity ID! */
    private int itemId;

    private int stackSize;

    /** Needed by Hibernate. */
    protected WildcardItemStack() {}

    public WildcardItemStack(int itemId, int stackSize) {
        this.itemId = itemId;
        this.stackSize = stackSize;
    }

    public int getItemId() {
        return itemId;
    }

    public int getStackSize() {
        return stackSize;
    }

    @Override
    public int compareTo(WildcardItemStack other) {
        return Comparator.comparing(WildcardItemStack::getItemId)
                .thenComparing(WildcardItemStack::getStackSize)
                .compare(this, other);
    }
}
