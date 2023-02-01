package com.github.dcysteine.nesql.sql.quest;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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

    @ElementCollection
    @OrderColumn
    private List<ItemStack> itemStacks;

    @Column(nullable = false)
    private String command;

    private int xp;

    private boolean levels;

    private int completeQuestId;

    /** Needed by Hibernate. */
    protected Reward() {}

    public Reward(
            String id, String name, RewardType type, List<ItemStack> itemStacks,
            String command, int xp, boolean levels, int completeQuestId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.itemStacks = itemStacks;
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

    public List<ItemStack> getItemStacks() {
        return itemStacks;
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