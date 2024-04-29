package com.github.dcysteine.nesql.sql.mobinfo;

import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.github.dcysteine.nesql.sql.base.item.ItemStackWithProbability;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

@Embeddable
@EqualsAndHashCode
@Getter
@ToString
public class MobDrop implements Comparable<MobDrop> {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    MobDropType type;

    @ManyToOne
    private Item item;

    private int stackSize;
    private double probability;
    private boolean lootable;
    private boolean playerOnly;

    /** Needed by Hibernate. */
    protected MobDrop() {}

    public MobDrop(
            MobDropType type, Item item, int stackSize,
            double probability, boolean lootable, boolean playerOnly) {
        this.type = type;
        this.item = item;
        this.stackSize = stackSize;
        this.probability = probability;
        this.lootable = lootable;
        this.playerOnly = playerOnly;
    }

    public ItemStack asItemStack() {
        return new ItemStack(item, stackSize);
    }

    public ItemStackWithProbability asItemStackWithProbability() {
        return new ItemStackWithProbability(item, stackSize, probability);
    }

    @Override
    public int compareTo(@NotNull MobDrop other) {
        return Comparator.comparing(MobDrop::getType)
                .thenComparing(MobDrop::getProbability)
                .thenComparing(MobDrop::getItem)
                .thenComparing(MobDrop::getStackSize)
                .thenComparing(MobDrop::isLootable)
                .thenComparing(MobDrop::isPlayerOnly)
                .compare(this, other);
    }
}
