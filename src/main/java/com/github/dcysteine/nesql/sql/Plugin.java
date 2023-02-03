package com.github.dcysteine.nesql.sql;

public enum Plugin {
    BASE("base"),
    NEI("nei"),
    FORGE("forge"),

    GREGTECH("gregtech"),
    THAUMCRAFT("thaumcraft"),
    QUEST("quest"),
    ;

    private final String name;

    Plugin(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
