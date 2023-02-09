package com.github.dcysteine.nesql.sql.gregtech;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.Metadata;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.List;

/**
 * Additional GregTech data associated with a recipe.
 *
 * <p>The actual recipe inputs and outputs are still stored in the {@link Recipe} table.
 */
@Entity
@EqualsAndHashCode
@ToString
public class GregTechRecipe implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @OneToOne
    private Recipe recipe;

    @Column(nullable = false)
    private String voltageTier;

    private int voltage;

    private int amperage;

    private int duration;

    private boolean requiresCleanroom;

    private boolean requiresLowGravity;

    @ManyToMany
    private List<Item> specialItems;

    @ElementCollection
    @OrderColumn
    private List<String> modOwners;

    @Column(length = Metadata.MAX_STRING_LENGTH, nullable = false)
    private String additionalInfo;

    /** Needed by Hibernate. */
    protected GregTechRecipe() {}

    public GregTechRecipe(
            String id,
            Recipe recipe,
            String voltageTier,
            int voltage,
            int amperage,
            int duration,
            boolean requiresCleanroom,
            boolean requiresLowGravity,
            List<Item> specialItems,
            List<String> modOwners,
            String additionalInfo) {
        this.id = id;
        this.recipe = recipe;
        this.voltageTier = voltageTier;
        this.voltage = voltage;
        this.amperage = amperage;
        this.duration = duration;
        this.requiresCleanroom = requiresCleanroom;
        this.requiresLowGravity = requiresLowGravity;
        this.specialItems = specialItems;
        this.modOwners = modOwners;
        this.additionalInfo = additionalInfo;
    }

    @Override
    public String getId() {
        return id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public String getVoltageTier() {
        return voltageTier;
    }

    public int getVoltage() {
        return voltage;
    }

    public int getAmperage() {
        return amperage;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isRequiresCleanroom() {
        return requiresCleanroom;
    }

    public boolean isRequiresLowGravity() {
        return requiresLowGravity;
    }

    public List<Item> getSpecialItems() {
        return specialItems;
    }

    public List<String> getModOwners() {
        return modOwners;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    @Override
    public int compareTo(Identifiable<String> other) {
        if (other instanceof GregTechRecipe) {
            return Comparator.comparing(GregTechRecipe::getRecipe)
                    .thenComparing(GregTechRecipe::getId)
                    .compare(this, (GregTechRecipe) other);
        } else {
            return Identifiable.super.compareTo(other);
        }
    }
}
