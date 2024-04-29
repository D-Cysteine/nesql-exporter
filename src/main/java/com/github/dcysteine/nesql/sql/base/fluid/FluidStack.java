package com.github.dcysteine.nesql.sql.base.fluid;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * A container class consisting of a {@link Fluid}, paired with an amount.
 * Used for defining recipes.
 */
@Embeddable
@EqualsAndHashCode
@Getter
@ToString
public class FluidStack implements Comparable<FluidStack> {
    @ManyToOne
    private Fluid fluid;

    private int amount;

    /** Needed by Hibernate. */
    protected FluidStack() {}

    public FluidStack(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.amount = amount;
    }

    @Override
    public int compareTo(@NotNull FluidStack other) {
        return Comparator.comparing(FluidStack::getFluid).thenComparing(FluidStack::getAmount)
                .compare(this, other);
    }
}
