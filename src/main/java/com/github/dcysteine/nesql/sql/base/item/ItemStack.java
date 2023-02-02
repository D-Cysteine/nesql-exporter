package com.github.dcysteine.nesql.sql.base.item;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;

/**
 * A container class consisting of an {@link Item}, paired with a stack size.
 * Used for defining recipes.
 */
@Embeddable
@EqualsAndHashCode
@ToString
public class ItemStack implements Comparable<ItemStack> {
    @ManyToOne
    private Item item;

    private int stackSize;

    /** Needed by Hibernate. */
    protected ItemStack() {}

    public ItemStack(Item item, int stackSize) {
        this.item = item;
        this.stackSize = stackSize;
    }

    public Item getItem() {
        return item;
    }

    public int getStackSize() {
        return stackSize;
    }

    @Override
    public int compareTo(ItemStack other) {
        return Comparator.comparing(ItemStack::getItem).thenComparing(ItemStack::getStackSize)
                .compare(this, other);
    }
}