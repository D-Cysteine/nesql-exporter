package com.github.dcysteine.nesql.sql.base.fluid;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * A container class consisting of a {@link Fluid}, an amount, and a probability.
 * Used for defining recipe outputs.
 */
@Embeddable
@EqualsAndHashCode
@Getter
@ToString
public class FluidStackWithProbability implements Comparable<FluidStackWithProbability> {
    @ManyToOne
    private Fluid fluid;

    private int amount;

    private double probability;

    /** Needed by Hibernate. */
    protected FluidStackWithProbability() {}

    public FluidStackWithProbability(Fluid fluid, int amount, double probability) {
        this.fluid = fluid;
        this.amount = amount;
        this.probability = probability;
    }

    public FluidStack withoutProbability() {
        return new FluidStack(fluid, amount);
    }

    @Override
    public int compareTo(@NotNull FluidStackWithProbability other) {
        return Comparator.comparing(FluidStackWithProbability::getFluid)
                .thenComparing(FluidStackWithProbability::getAmount)
                .thenComparing(FluidStackWithProbability::getProbability)
                .compare(this, other);
    }
}
