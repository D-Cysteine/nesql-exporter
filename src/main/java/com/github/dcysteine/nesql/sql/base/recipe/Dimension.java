package com.github.dcysteine.nesql.sql.base.recipe;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;

/** Holds the dimensions of an input or output grid for a recipe type. */
@Embeddable
@EqualsAndHashCode
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public int compareTo(Dimension other) {
        return Comparator.comparing(Dimension::getWidth)
                .thenComparing(Dimension::getHeight)
                .compare(this, other);
    }
}
