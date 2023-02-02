package com.github.dcysteine.nesql.sql.gregtech;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.recipe.Recipe;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    @OneToOne(fetch = FetchType.LAZY)
    private Recipe recipe;

    @ElementCollection
    @OrderColumn
    private List<String> modOwners;

    private int duration;

    private int euPerTick;

    @Column(nullable = false)
    private String voltageTier;

    private boolean requiresCleanroom;

    private boolean requiresLowGravity;

    @Lob
    @Column(nullable = false)
    private String additionalInfo;

    /** Needed by Hibernate. */
    protected GregTechRecipe() {}

    public GregTechRecipe(
            String id,
            Recipe recipe,
            List<String> modOwners,
            int duration,
            int euPerTick,
            String voltageTier,
            boolean requiresCleanroom,
            boolean requiresLowGravity,
            String additionalInfo) {
        this.id = id;
        this.recipe = recipe;
        this.modOwners = modOwners;
        this.duration = duration;
        this.euPerTick = euPerTick;
        this.voltageTier = voltageTier;
        this.requiresCleanroom = requiresCleanroom;
        this.requiresLowGravity = requiresLowGravity;
        this.additionalInfo = additionalInfo;
    }

    @Override
    public String getId() {
        return id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public List<String> getModOwners() {
        return modOwners;
    }

    public int getDuration() {
        return duration;
    }

    public int getEuPerTick() {
        return euPerTick;
    }

    public String getVoltageTier() {
        return voltageTier;
    }

    public boolean isRequiresCleanroom() {
        return requiresCleanroom;
    }

    public boolean isRequiresLowGravity() {
        return requiresLowGravity;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }
}
