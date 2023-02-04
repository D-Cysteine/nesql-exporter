package com.github.dcysteine.nesql.sql.base.item;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
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

    /**
     * Whether this wildcard item stack has wildcard item damage. If this field is true, then
     * {@link #itemDamage} will be unset.
     */
    private boolean wildcardItemDamage;

    /** The item damage, if this wildcard item stack does not have wildcard item damage. */
    private int itemDamage;

    /**
     * Whether this wildcard item stack has wildcard NBT. If this field is true, then {@link #nbt}
     * will be unset.
     */
    private boolean wildcardNbt;

    /** The item damage, if this wildcard item stack does not have wildcard item damage. */
    @Lob
    private String nbt;

    private int stackSize;

    /** Needed by Hibernate. */
    protected WildcardItemStack() {}

    public WildcardItemStack(
            String modId, String internalName, int itemId,
            boolean wildcardItemDamage, int itemDamage, boolean wildcardNbt, String nbt,
            int stackSize) {
        this.modId = modId;
        this.internalName = internalName;
        this.itemId = itemId;
        this.wildcardItemDamage = wildcardItemDamage;
        this.itemDamage = itemDamage;
        this.wildcardNbt = wildcardNbt;
        this.nbt = nbt;
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

    public boolean isWildcardItemDamage() {
        return wildcardItemDamage;
    }

    public int getItemDamage() {
        return itemDamage;
    }

    public boolean isWildcardNbt() {
        return wildcardNbt;
    }

    public String getNbt() {
        return nbt;
    }

    public int getStackSize() {
        return stackSize;
    }

    @Override
    public int compareTo(WildcardItemStack other) {
        return Comparator.comparing(WildcardItemStack::getModId)
                .thenComparing(WildcardItemStack::getInternalName)
                .thenComparing(WildcardItemStack::isWildcardItemDamage)
                .thenComparing(WildcardItemStack::getItemDamage)
                .thenComparing(WildcardItemStack::isWildcardNbt)
                .thenComparing(WildcardItemStack::getNbt)
                .thenComparing(WildcardItemStack::getStackSize)
                .compare(this, other);
    }
}
