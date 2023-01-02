package com.github.dcysteine.nesql.sql.base.recipe;

import com.google.auto.value.AutoValue;

public enum RecipeType {
    MINECRAFT_SHAPED_CRAFTING(
            false,
            Dimension.create(3, 3),
            Dimension.create(0, 0),
            Dimension.create(1, 1),
            Dimension.create(0, 0)),

    MINECRAFT_SHAPED_CRAFTING_OREDICT(
            false,
            Dimension.create(3, 3),
            Dimension.create(0, 0),
            Dimension.create(1, 1),
            Dimension.create(0, 0)),

    MINECRAFT_SHAPELESS_CRAFTING(
            true,
            Dimension.create(3, 3),
            Dimension.create(0, 0),
            Dimension.create(1, 1),
            Dimension.create(0, 0)),

    MINECRAFT_SHAPELESS_CRAFTING_OREDICT(
            true,
            Dimension.create(3, 3),
            Dimension.create(0, 0),
            Dimension.create(1, 1),
            Dimension.create(0, 0)),

    MINECRAFT_FURNACE(
            true,
            Dimension.create(1, 1),
            Dimension.create(0, 0),
            Dimension.create(1, 1),
            Dimension.create(0, 0)),
    ;

    public final boolean shapeless;
    // TODO move the rest of this to server display object
    public final Dimension itemInputDimensions;
    public final Dimension fluidInputDimensions;
    public final Dimension itemOutputDimensions;
    public final Dimension fluidOutputDimensions;

    private RecipeType(
            boolean shapeless,
            Dimension itemInputDimensions, Dimension fluidInputDimensions,
            Dimension itemOutputDimensions, Dimension fluidOutputDimensions) {
        this.shapeless = shapeless;
        this.itemInputDimensions = itemInputDimensions;
        this.fluidInputDimensions = fluidInputDimensions;
        this.itemOutputDimensions = itemOutputDimensions;
        this.fluidOutputDimensions = fluidOutputDimensions;
    }

    /**
     * Class specifying dimensions for a grid, to display recipe inputs or outputs.
     *
     * <p>{@link #EMPTY_DIMENSION}, which has size {@code (0, 0)}, indicates that the recipe does
     * not have the given input / output type.
     */
    @AutoValue
    public abstract static class Dimension {
        public static final Dimension EMPTY_DIMENSION = create(0, 0);

        public static Dimension create(int width, int height) {
            return new AutoValue_RecipeType_Dimension(width, height);
        }

        public abstract int width();

        public abstract int height();
    }
}
