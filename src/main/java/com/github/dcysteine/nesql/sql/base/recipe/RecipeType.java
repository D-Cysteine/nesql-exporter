package com.github.dcysteine.nesql.sql.base.recipe;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SortNatural;

import java.util.SortedSet;

/** Contains information about a type of recipe. */
@Entity
@EqualsAndHashCode(exclude = "recipes")  // Prevent stack overflow
@ToString
public class RecipeType implements Identifiable<String> {
    @Id
    private String id;

    /** Human-readable string describing the recipe's general category, such as originating mod. */
    @Column(nullable = false)
    private String category;

    /** Human-readable string describing the specific type of recipe. */
    @Column(nullable = false)
    private String type;

    @ManyToOne
    private Item icon;

    private boolean shapeless;

    @Embedded
    private Dimension itemInputDimension;

    @Embedded
    private Dimension fluidInputDimension;

    @Embedded
    private Dimension itemOutputDimension;

    @Embedded
    private Dimension fluidOutputDimension;

    @OneToMany(mappedBy = "recipeType")
    @SortNatural
    private SortedSet<Recipe> recipes;

    /** Needed by Hibernate. */
    protected RecipeType() {}

    /**
     * Note that {@code category} and {@code type} together form the primary key for this entity, so
     * make sure that their combination is unique!
     */
    public RecipeType(
            String category, String type, Item icon, boolean shapeless,
            Dimension itemInputDimension, Dimension fluidInputDimension,
            Dimension itemOutputDimension, Dimension fluidOutputDimension) {
        this.id = String.format("%s~%s", category, type);
        this.category = category;
        this.type = type;
        this.icon = icon;
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

    public SortedSet<Recipe> getRecipes() {
        return recipes;
    }
}
