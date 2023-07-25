package com.github.dcysteine.nesql.sql.quest;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.entity.Entity;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * This class contains data for the various types of BetterQuesting tasks.
 *
 * <p>Each field will only be set for certain task types. See {@link TaskType} for details.
 */
@jakarta.persistence.Entity
@EqualsAndHashCode
@ToString
public class Task implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    /** This field is always set. */
    @Column(nullable = false)
    private String name;

    /** This field is always set. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType type;

    @ManyToMany
    @OrderColumn
    private List<ItemGroup> items;

    @ElementCollection
    @OrderColumn
    private List<FluidStack> fluids;

    @ManyToOne
    private Entity entity;

    private boolean consume;

    private int numberRequired;

    @Column(nullable = false)
    private String dimensionName;

    /** Needed by Hibernate. */
    protected Task() {}

    public Task(
            String id, String name, TaskType type,
            List<ItemGroup> items, List<FluidStack> fluids,
            boolean consume, Entity entity, int numberRequired, String dimensionName) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.items = items;
        this.fluids = fluids;
        this.consume = consume;
        this.entity = entity;
        this.numberRequired = numberRequired;
        this.dimensionName = dimensionName;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TaskType getType() {
        return type;
    }

    public List<ItemGroup> getItems() {
        return items;
    }

    public List<FluidStack> getFluids() {
        return fluids;
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isConsume() {
        return consume;
    }

    public int getNumberRequired() {
        return numberRequired;
    }

    public String getDimensionName() {
        return dimensionName;
    }
}