package com.github.dcysteine.nesql.sql;

public abstract class Identifiable<K extends Comparable<K>> implements Comparable<Identifiable<K>> {
    public abstract K getId();

    @Override
    public int compareTo(Identifiable<K> other) {
        return getId().compareTo(other.getId());
    }
}
