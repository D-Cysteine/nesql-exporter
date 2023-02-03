package com.github.dcysteine.nesql.sql.forge;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

/** Holds an association between a fluid and its registered containers. */
@Entity
@EqualsAndHashCode
@ToString
public class FluidContainer implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @OneToOne
    private Fluid fluid;

    /** Map of fluid container to container capacity. */
    @ElementCollection
    private Map<Item, Integer> containers;

    /** Needed by Hibernate. */
    protected FluidContainer() {}

    public FluidContainer(String id, Fluid fluid, Map<Item, Integer> containers) {
        this.id = id;
        this.fluid = fluid;
        this.containers = containers;
    }

    @Override
    public String getId() {
        return id;
    }

    public Fluid getFluid() {
        return fluid;
    }

    public Map<Item, Integer> getContainers() {
        return containers;
    }
}
