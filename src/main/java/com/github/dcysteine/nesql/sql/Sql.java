package com.github.dcysteine.nesql.sql;

/**
 * Dummy class whose purpose is to serve as a target for {@code @ComponentScan}.
 * Since we have this, may as well put some constants here, too.
 */
public class Sql {
    private Sql() {}

    /** Maximum length for long string fields, such as NBT. */
    public static final int LONG_STRING_MAX_LENGTH = 4000;

    /**
     * Maximum length for extremely long string fields, such as quest descriptions.
     *
     * <p>8000 might be the maximum that a column can hold.
     * To increase this any further, add {@code @Lob} to store the data as a blob, instead.
     *
     * <p>See:
     * https://stackoverflow.com/questions/16414215/jpa-how-to-set-string-column-to-varcharmax-in-ddl
     */
    public static final int EXTREME_STRING_MAX_LENGTH = 8000;
}
