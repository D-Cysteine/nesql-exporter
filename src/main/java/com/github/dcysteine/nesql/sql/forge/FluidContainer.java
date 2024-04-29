package com.github.dcysteine.nesql.sql.forge;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Comparator;

/** Holds data for a fluid container. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class FluidContainer implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private FluidStack fluidStack;

    @OneToOne
    private Item container;

    @ManyToOne
    private Item emptyContainer;

    /** Needed by Hibernate. */
    protected FluidContainer() {}

    public FluidContainer(String id, FluidStack fluidStack, Item container, Item emptyContainer) {
        this.id = id;
        this.fluidStack = fluidStack;
        this.container = container;
        this.emptyContainer = emptyContainer;
    }

    @Override
    public int compareTo(Identifiable<String> other) {
        if (other instanceof FluidContainer) {
            return Comparator.comparing(FluidContainer::getFluidStack)
                    .thenComparing(FluidContainer::getEmptyContainer)
                    .thenComparing(FluidContainer::getContainer)
                    .thenComparing(FluidContainer::getId)
                    .compare(this, (FluidContainer) other);
        } else {
            return Identifiable.super.compareTo(other);
        }
    }
}
