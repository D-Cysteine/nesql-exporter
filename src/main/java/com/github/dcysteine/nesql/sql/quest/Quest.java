package com.github.dcysteine.nesql.sql.quest;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.Sql;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Set;

/** Holds information about a BetterQuesting quest. */
@Entity
@EqualsAndHashCode
@ToString
public class Quest implements Identifiable<Integer> {
    @Id
    private int id;

    @ManyToOne
    private Item icon;

    @Column(nullable = false)
    private String name;

    @Column(length = Sql.EXTREME_STRING_MAX_LENGTH, nullable = false)
    private String description;

    /** Quests that this quest requires. */
    @EqualsAndHashCode.Exclude
    @ManyToMany
    private Set<Quest> requiredQuests;

    /** Quests that require this quest. */
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "requiredQuests")
    private Set<Quest> requiredByQuests;

    @ElementCollection
    @OrderColumn
    private List<String> tasks;

    @ElementCollection
    @OrderColumn
    private List<String> rewards;

    /** Needed by Hibernate. */
    protected Quest() {}

    /**
     * We'll set {@link #requiredQuests} in a second pass,
     * after all quests have been committed to the DB.
     */
    public Quest(
            int id, Item icon, String name, String description,
            List<String> tasks, List<String> rewards) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.tasks = tasks;
        this.rewards = rewards;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public Item getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setRequiredQuests(Set<Quest> requiredQuests) {
        this.requiredQuests = requiredQuests;
    }

    public Set<Quest> getRequiredQuests() {
        return requiredQuests;
    }

    public Set<Quest> getRequiredByQuests() {
        return requiredByQuests;
    }

    public List<String> getTasks() {
        return tasks;
    }

    public List<String> getRewards() {
        return rewards;
    }
}
