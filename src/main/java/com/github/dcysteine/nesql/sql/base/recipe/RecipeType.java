package com.github.dcysteine.nesql.sql.base.recipe;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** Contains information about a type of recipe. */
@Entity
@EqualsAndHashCode
@ToString
public class RecipeType implements Identifiable<String> {
    /**
     * Short string that needs to be unique for each recipe type.
     *
     * <p>This doesn't need to be human-readable. It's recommended to make it vaguely
     * understandable, but as short as possible, and not containing non-URL-safe characters.
     *
     * <p>Probably a good idea to prefix with the plugin name, to help with uniqueness.
     */
    @Id
    private String id;

    /** String describing the recipe's general category, such as originating mod or plugin. */
    @Column(nullable = false)
    private String category;

    /** String describing the specific type of recipe. */
    @Column(nullable = false)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item icon;

    /** Additional info to show on the icon. */
    @Column(nullable = false)
    private String iconInfo;

    private boolean shapeless;

    @Embedded
    private Dimension itemInputDimension;

    @Embedded
    private Dimension fluidInputDimension;

    @Embedded
    private Dimension itemOutputDimension;

    @Embedded
    private Dimension fluidOutputDimension;

    /** Needed by Hibernate. */
    protected RecipeType() {}

    /**
     * Note that {@code category} and {@code type} together form the primary key for this entity, so
     * make sure that their combination is unique!
     */
    public RecipeType(
            String id, String category, String type, Item icon, String iconInfo, boolean shapeless,
            Dimension itemInputDimension, Dimension fluidInputDimension,
            Dimension itemOutputDimension, Dimension fluidOutputDimension) {
        this.id = id;
        this.category = category;
        this.type = type;
        this.icon = icon;
        this.iconInfo = iconInfo;
        this.shapeless = shapeless;
        this.itemInputDimension = itemInputDimension;
        this.fluidInputDimension = fluidInputDimension;
        this.itemOutputDimension = itemOutputDimension;
        this.fluidOutputDimension = fluidOutputDimension;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public Item getIcon() {
        return icon;
    }

    public String getIconInfo() {
        return iconInfo;
    }

    public boolean isShapeless() {
        return shapeless;
    }

    public Dimension getItemInputDimension() {
        return itemInputDimension;
    }

    public Dimension getFluidInputDimension() {
        return fluidInputDimension;
    }

    public Dimension getItemOutputDimension() {
        return itemOutputDimension;
    }

    public Dimension getFluidOutputDimension() {
        return fluidOutputDimension;
    }
}
