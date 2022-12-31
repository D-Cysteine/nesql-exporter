package com.github.dcysteine.nesql.sql;

public interface Identifiable<K extends Comparable<K>> extends Comparable<Identifiable<K>> {
    K getId();

    @Override
    default int compareTo(Identifiable<K> other) {
        return getId().compareTo(other.getId());
    }
}
