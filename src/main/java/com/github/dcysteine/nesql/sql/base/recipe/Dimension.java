package com.github.dcysteine.nesql.sql.base.recipe;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/** Holds the dimensions of an input or output grid for a recipe type. */
@Embeddable
@EqualsAndHashCode
@Getter
@ToString
public class Dimension implements Comparable<Dimension> {
    private int width;
    private int height;

    /** Needed by Hibernate. */
    protected Dimension() {}

    public Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public int compareTo(@NotNull Dimension other) {
        return Comparator.comparing(Dimension::getWidth)
                .thenComparing(Dimension::getHeight)
                .compare(this, other);
    }
}
