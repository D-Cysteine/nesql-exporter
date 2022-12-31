package com.github.dcysteine.nesql.sql;

/**
 * Dummy class whose purpose is to serve as a target for {@code @ComponentScan}.
 * Since we have this, may as well put some constants here, too.
 */
public class Sql {
    private Sql() {}

    /** Maximum length for long string fields, such as NBT. */
    public static final int STRING_MAX_LENGTH = 2048;
}
