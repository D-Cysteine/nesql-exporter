package com.github.dcysteine.nesql.sql.quest;

/**
 * This enum contains the various types of rewards.
 * Each task type will set different fields on the {@link Reward} object.
 */
public enum RewardType {
    /** Sets the {@code items} field. */
    ITEM("item"),

    /** Sets the {@code items} field. */
    CHOICE("choice"),

    /** Sets the {@code command} field. */
    COMMAND("command"),

    /** Sets the {@code xp} and {@code levels} fields. */
    XP("xp"),

    /** Sets the {@code completeQuestId} field. */
    COMPLETE_QUEST("complete quest"),

    /** Does not set any fields. */
    UNHANDLED("unhandled"),
    ;

    private final String name;

    RewardType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
