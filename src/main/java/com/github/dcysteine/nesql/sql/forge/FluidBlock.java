package com.github.dcysteine.nesql.sql.forge;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/** Holds an association between a fluid and its block form. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class FluidBlock implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @OneToOne
    private Fluid fluid;

    @OneToOne
    private Item block;

    /** Needed by Hibernate. */
    protected FluidBlock() {}

    public FluidBlock(String id, Fluid fluid, Item block) {
        this.id = id;
        this.fluid = fluid;
        this.block = block;
    }
}
