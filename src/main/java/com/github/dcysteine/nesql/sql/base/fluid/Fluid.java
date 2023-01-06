package com.github.dcysteine.nesql.sql.base.fluid;

import com.github.dcysteine.nesql.sql.Identifiable;

import com.github.dcysteine.nesql.sql.Sql;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.Comparator;

@Entity
@EqualsAndHashCode
@ToString
public class Fluid implements Identifiable<String> {
    /**
     * This is the unique table key, NOT the Forge fluid ID! The latter is not unique (there can be
     * multiple fluid rows for the same Forge fluid ID).
     */
    @Id
    @Column(length = Sql.STRING_MAX_LENGTH, nullable = false)
    private String id;

    @Column(nullable = false)
    private String imageFilePath;

    @Column(nullable = false)
    private String internalName;

    @Column(nullable = false)
    private String unlocalizedName;

    @Column(nullable = false)
    private String localizedName;

    /**
     * The Forge fluid ID.
     */
    private int fluidId;

    @Nullable
    @Column(length = Sql.STRING_MAX_LENGTH)
    private String nbt;

    /** Needed by Hibernate. */
    protected Fluid() {}

    public Fluid(
            String id,
            String imageFilePath,
            String internalName,
            String unlocalizedName,
            String localizedName,
            int fluidId,
            @Nullable String nbt) {
        this.id = id;
        this.imageFilePath = imageFilePath;
        this.internalName = internalName;
        this.unlocalizedName = unlocalizedName;
        this.localizedName = localizedName;
        this.fluidId = fluidId;
        this.nbt = nbt;
    }

    /**
     * This is the unique table key, NOT the Minecraft item ID! The latter is not unique (there can
     * be multiple item rows for the same Minecraft item ID).
     */
    @Override
    public String getId() {
        return id;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    /** The Forge fluid ID. */
    public int getFluidId() {
        return fluidId;
    }

    public boolean hasNbt() {
        return nbt != null;
    }

    @Nullable
    public String getNbt() {
        return nbt;
    }

    @Override
    public int compareTo(Identifiable<String> other) {
        if (other instanceof Fluid) {
            return Comparator.comparing(Fluid::getFluidId)
                    .thenComparing(Fluid::getNbt, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(Fluid::getId)
                    .compare(this, (Fluid) other);
        } else {
            return Identifiable.super.compareTo(other);
        }
    }
}
