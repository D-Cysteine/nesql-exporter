package com.github.dcysteine.nesql.sql;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

/** Contains metadata about the repository. */
@Entity
@EqualsAndHashCode
@ToString
public class Metadata implements Identifiable<Integer> {
    /** There will only ever be one metadata entry, and it will have this ID. */
    public static final int ID = 0;

    /**
     * This constant will be replaced with the NESQL version by the buildscript.
     *
     * <p>The server can check this field to know its version of the SQL schema, and check the
     * {@link #version} field on the {@code Metadata} row to know the version of the repository.
     */
    public static final String VERSION = "@version@";

    /** There will only ever be one metadata entry, and it will have ID {@link #ID}. */
    @Id
    private int id;

    @Column(nullable = false)
    private String version;

    /** When this database was created, in epoch milliseconds. */
    @Column(nullable = false)
    private long creationTimeMillis;

    /** Set of active plugins. */
    @Enumerated(EnumType.STRING)
    @ElementCollection
    private Set<Plugin> activePlugins;

    /** Needed by Hibernate. */
    protected Metadata() {}

    public Metadata(Set<Plugin> activePlugins) {
        this.id = ID;
        this.version = VERSION;
        this.creationTimeMillis = System.currentTimeMillis();
        this.activePlugins = activePlugins;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public long getCreationTimeMillis() {
        return creationTimeMillis;
    }

    public Set<Plugin> getActivePlugins() {
        return activePlugins;
    }
}
