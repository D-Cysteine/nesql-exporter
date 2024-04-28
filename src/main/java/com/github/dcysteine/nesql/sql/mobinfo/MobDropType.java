package com.github.dcysteine.nesql.sql.mobinfo;

public enum MobDropType {
    NORMAL("normal"),
    RARE("rare"),
    ADDITIONAL("additional"),
    INFERNAL("infernal"),
    ;

    private final String name;

    MobDropType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
