package com.github.dcysteine.nesql.sql.forge;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

/** Holds an association between an empty fluid container and its filled containers. */
@Entity
@EqualsAndHashCode
@ToString
public class EmptyContainer implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @OneToOne
    private Item emptyContainer;

    /** Map of fluid container to container capacity. */
    @ElementCollection
    private Map<Item, Integer> containers;

    /** Needed by Hibernate. */
    protected EmptyContainer() {}

    public EmptyContainer(String id, Item emptyContainer, Map<Item, Integer> containers) {
        this.id = id;
        this.emptyContainer = emptyContainer;
        this.containers = containers;
    }

    @Override
    public String getId() {
        return id;
    }

    public Item getEmptyContainer() {
        return emptyContainer;
    }

    public Map<Item, Integer> getContainers() {
        return containers;
    }
}
