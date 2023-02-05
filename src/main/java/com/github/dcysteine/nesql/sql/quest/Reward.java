package com.github.dcysteine.nesql.sql.quest;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OrderColumn;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * This class contains data for the various types of BetterQuesting rewards.
 *
 * <p>Each field will only be set for certain reward types. See {@link RewardType} for details.
 */
@Entity
@EqualsAndHashCode
@ToString
public class Reward implements Identifiable<String> {
    @Id
    private String id;

    /** This field is always set. */
    @Column(nullable = false)
    private String name;

    /** This field is always set. */
    @Enumerated(EnumType.STRING)
    private RewardType type;

    @ManyToMany
    @OrderColumn
    private List<ItemGroup> items;

    @Column(nullable = false)
    private String command;

    private int xp;

    private boolean levels;

    private int completeQuestId;

    /** Needed by Hibernate. */
    protected Reward() {}

    public Reward(
            String id, String name, RewardType type, List<ItemGroup> items,
            String command, int xp, boolean levels, int completeQuestId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.items = items;
        this.command = command;
        this.xp = xp;
        this.levels = levels;
        this.completeQuestId = completeQuestId;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RewardType getType() {
        return type;
    }

    public List<ItemGroup> getItems() {
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

    public int getCompleteQuestId() {
        return completeQuestId;
    }
}