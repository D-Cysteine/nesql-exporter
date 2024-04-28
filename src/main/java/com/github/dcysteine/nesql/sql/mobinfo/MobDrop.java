package com.github.dcysteine.nesql.sql.mobinfo;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;

@Entity
@EqualsAndHashCode
@ToString
public class MobDrop implements Identifiable<String> {
    /**
     * Unfortunately, I had to include an integer index in this ID in order to disambiguate when we
     * have multiple very similar mob drop entries.
     *
     * <p>As a consequence, these IDs might not be stable, and may vary with each export, or when
     * the pack is updated.
     */
    @Id
    @Column(nullable = false)
    private String id;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    MobInfo mobInfo;

    /** This field is always set. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    MobDropType type;

    ItemStack itemStack;

    private double chance;
    private boolean lootable;
    private boolean playerOnly;

    /** Needed by Hibernate. */
    protected MobDrop() {}

    public MobDrop(
            String id, MobInfo mobInfo, MobDropType type, ItemStack itemStack,
            double chance, boolean lootable, boolean playerOnly) {
        this.id = id;
        this.mobInfo = mobInfo;
        this.type = type;
        this.itemStack = itemStack;
        this.chance = chance;
        this.lootable = lootable;
        this.playerOnly = playerOnly;
    }

    @Override
    public String getId() {
        return id;
    }

    public MobInfo getMobInfo() {
        return mobInfo;
    }

    public MobDropType getType() {
        return type;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public double getChance() {
        return chance;
    }

    public boolean isLootable() {
        return lootable;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    @Override
    public int compareTo(Identifiable<String> other) {
        if (other instanceof MobDrop) {
            return Comparator.comparing(MobDrop::getMobInfo)
                    .thenComparing(MobDrop::getType)
                    .thenComparing(MobDrop::getChance)
                    .thenComparing(MobDrop::getId)
                    .compare(this, (MobDrop) other);
        } else {
            return Identifiable.super.compareTo(other);
        }
    }
}
