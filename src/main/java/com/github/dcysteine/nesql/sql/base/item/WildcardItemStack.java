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
    private String modId;

    private String internalName;

    /**
     * The Minecraft item ID. These IDs can vary from world to world, so don't rely on them!
     *
     * <p>However, we should be able to use this field to query for members of a wildcard item
     * stack, since the IDs will be internally consistent within the export.
     */
    private int itemId;

    private int stackSize;

    /** Needed by Hibernate. */
    protected WildcardItemStack() {}

    public WildcardItemStack(String modId, String internalName, int itemId, int stackSize) {
        this.modId = modId;
        this.itemId = itemId;
        this.internalName = internalName;
        this.stackSize = stackSize;
    }

    public String getModId() {
        return modId;
    }

    public String getInternalName() {
        return internalName;
    }

    /**
     * The Minecraft item ID. These IDs can vary from world to world, so don't rely on them!
     *
     * <p>However, we should be able to use this field to query for members of a wildcard item
     * stack, since the IDs will be internally consistent within the export.
     */
    public int getItemId() {
        return itemId;
    }

    public int getStackSize() {
        return stackSize;
    }

    @Override
    public int compareTo(WildcardItemStack other) {
        return Comparator.comparing(WildcardItemStack::getModId)
                .thenComparing(WildcardItemStack::getInternalName)
                .thenComparing(WildcardItemStack::getStackSize)
                .compare(this, other);
    }
}
