package com.github.dcysteine.nesql.sql.quest;

import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OrderColumn;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.List;

/**
 * This class contains data for all of the various types of BetterQuesting rewards.
 *
 * <p>Each field will only be set for certain reward types. See {@link RewardType} for details.
 */
@Embeddable
@EqualsAndHashCode
@ToString
public class Reward implements Comparable<Reward> {
    /** This field is always set. */
    @Column(nullable = false)
    private String name;

    /** This field is always set. */
    @Enumerated(EnumType.STRING)
    private RewardType type;

    @ManyToMany
    @OrderColumn
    private List<Item> items;

    @Column(nullable = false)
    private String command;

    private int xp;

    private boolean levels;

    /** Needed by Hibernate. */
    protected Reward() {}

    public Reward(
            String name, RewardType type, List<Item> items,
            String command, int xp, boolean levels) {
        this.name = name;
        this.type = type;
        this.items = items;
        this.command = command;
        this.xp = xp;
        this.levels = levels;
    }

    public String getName() {
        return name;
    }

    public RewardType getType() {
        return type;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getCommand() {
        return command;
    }

    public int getXp() {
        return xp;
    }

    public boolean isLevels() {
        return levels;
    }

    @Override
    public int compareTo(Reward other) {
        return Comparator.comparing(Reward::getType).compare(this, other);
    }
}