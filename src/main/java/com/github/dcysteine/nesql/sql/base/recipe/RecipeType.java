package com.github.dcysteine.nesql.sql.base.recipe;

public enum RecipeType {
    MINECRAFT_SHAPED_CRAFTING(false),
    MINECRAFT_SHAPED_CRAFTING_OREDICT(false),
    MINECRAFT_SHAPELESS_CRAFTING(true),
    MINECRAFT_SHAPELESS_CRAFTING_OREDICT(true),
    MINECRAFT_FURNACE(true),
    ;

    public final boolean shapeless;

    RecipeType(boolean shapeless) {
        this.shapeless = shapeless;
    }

    public boolean isShapeless() {
        return shapeless;
    }
}
