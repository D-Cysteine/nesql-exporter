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
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * This class contains data for the various types of BetterQuesting rewards.
 *
 * <p>Each field will only be set for certain reward types. See {@link RewardType} for details.
 */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class Reward implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    /** This field is always set. */
    @Column(nullable = false)
    private String name;

    /** This field is always set. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardType type;

    @ManyToMany
    @OrderColumn
    private List<ItemGroup> items;

    @Column(nullable = false)
    private String command;

    private int xp;

    private boolean levels;

    @Column(nullable = false)
    private String completeQuestId;

    /** Needed by Hibernate. */
    protected Reward() {}

    public Reward(
            String id, String name, RewardType type, List<ItemGroup> items,
            String command, int xp, boolean levels, String completeQuestId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.items = items;
        this.command = command;
        this.xp = xp;
        this.levels = levels;
        this.completeQuestId = completeQuestId;
    }
}