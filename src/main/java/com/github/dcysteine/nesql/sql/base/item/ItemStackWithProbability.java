package com.github.dcysteine.nesql.sql.base.item;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;

/**
 * A container class consisting of an {@link Item}, a stack size, and a probability.
 * Used for defining recipe outputs.
 */
@Embeddable
@EqualsAndHashCode
@ToString
public class ItemStackWithProbability implements Comparable<ItemStackWithProbability> {
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    private int stackSize;

    private double probability;

    /** Needed by Hibernate. */
    protected ItemStackWithProbability() {}

    public ItemStackWithProbability(Item item, int stackSize, double probability) {
        this.item = item;
        this.stackSize = stackSize;
        this.probability = probability;
    }

    public Item getItem() {
        return item;
    }

    public int getStackSize() {
        return stackSize;
    }

    public double getProbability() {
        return probability;
    }

    public ItemStack withoutProbability() {
        return new ItemStack(item, stackSize);
    }

    @Override
    public int compareTo(ItemStackWithProbability other) {
        return Comparator.comparing(ItemStackWithProbability::getItem)
                .thenComparing(ItemStackWithProbability::getStackSize)
                .thenComparing(ItemStackWithProbability::getProbability)
                .compare(this, other);
    }
}