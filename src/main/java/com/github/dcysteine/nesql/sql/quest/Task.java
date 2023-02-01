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
 * This class contains data for all of the various types of BetterQuesting tasks.
 *
 * <p>Each field will only be set for certain task types. See {@link TaskType} for details.
 */
@Embeddable
@EqualsAndHashCode
@ToString
public class Task implements Comparable<Task> {
    /** This field is always set. */
    @Column(nullable = false)
    private String name;

    /** This field is always set. */
    @Enumerated(EnumType.STRING)
    private TaskType type;

    @ManyToMany
    @OrderColumn
    private List<Item> items;

    @Column(nullable = false)
    private String entityId;

    private int numberRequired;

    /** Needed by Hibernate. */
    protected Task() {}

    public Task(String name, TaskType type, List<Item> items, String entityId, int numberRequired) {
        this.name = name;
        this.type = type;
        this.items = items;
        this.entityId = entityId;
        this.numberRequired = numberRequired;
    }

    public String getName() {
        return name;
    }

    public TaskType getType() {
        return type;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getEntityId() {
        return entityId;
    }

    public int getNumberRequired() {
        return numberRequired;
    }

    @Override
    public int compareTo(Task other) {
        return Comparator.comparing(Task::getType).compare(this, other);
    }
}