package com.github.dcysteine.nesql.sql.quest;

/**
 * This enum contains the various types of tasks.
 * Each task type will set different fields on the {@link Task} object.
 */
public enum TaskType {
    /** Sets the {@code itemStacks} field. */
    RETRIEVAL("retrieval"),

    /** Sets the {@code itemStacks} field. */
    CRAFTING("crafting"),

    /** Sets the {@code fluidStacks} field. */
    FLUID("fluid"),

    /** Does not set any fields. */
    CHECKBOX("checkbox"),

    /** Sets the {@code entityId} and {@code numberRequired} fields. */
    HUNT("hunt"),

    /** Sets the {@code dimensionName} field. */
    LOCATION("location"),

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
