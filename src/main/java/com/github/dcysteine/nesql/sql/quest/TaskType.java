package com.github.dcysteine.nesql.sql.quest;

/**
 * This enum contains the various types of tasks.
 * Each task type will set different fields on the {@link Task} object.
 */
public enum TaskType {
    /** Sets the {@code items} field. */
    RETRIEVAL("retrieval"),

    /** Sets the {@code items} field. */
    CRAFTING("crafting"),

    /** Does not set any fields. */
    CHECKBOX("checkbox"),

    /** Sets the {@code entityId} and {@code numberRequired} fields. */
    HUNT("hunt"),

    /** Does not set any fields. */
    UNHANDLED("unhandled"),
    ;
    private final String name;

    TaskType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
