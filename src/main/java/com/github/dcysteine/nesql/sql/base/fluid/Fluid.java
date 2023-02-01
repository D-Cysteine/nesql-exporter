package com.github.dcysteine.nesql.sql.base.fluid;

import com.github.dcysteine.nesql.sql.Identifiable;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String imageFilePath;

    @Column(nullable = false)
    private String modId;

    @Column(nullable = false)
    private String internalName;

    @Column(nullable = false)
    private String unlocalizedName;

    @Column(nullable = false)
    private String localizedName;

    /** The Forge fluid ID. These are regenerated on game startup, and so are not stable! */
    private int fluidId;

    @Lob
    @Column(nullable = false)
    private String nbt;

    private int luminosity;
    private int density;
    private int temperature;
    private int viscosity;
    private boolean gaseous;

    /** Needed by Hibernate. */
    protected Fluid() {}

    public Fluid(
            String id,
            String imageFilePath,
            String modId,
            String internalName,
            String unlocalizedName,
            String localizedName,
            int fluidId,
            String nbt,
            int luminosity,
            int density,
            int temperature,
            int viscosity,
            boolean gaseous) {
        this.id = id;
        this.imageFilePath = imageFilePath;
        this.modId = modId;
        this.internalName = internalName;
        this.unlocalizedName = unlocalizedName;
        this.localizedName = localizedName;
        this.fluidId = fluidId;
        this.nbt = nbt;
        this.luminosity = luminosity;
        this.density = density;
        this.temperature = temperature;
        this.viscosity = viscosity;
        this.gaseous = gaseous;
    }

    /**
     * This is the unique table key, NOT the Forge fluid ID! The latter is not unique (there can be
     * multiple fluid rows for the same Forge fluid ID).
     */
    @Override
    public String getId() {
        return id;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public String getModId() {
        return modId;
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

    /** The Forge fluid ID. These are regenerated on game startup, and so are not stable! */
    public int getFluidId() {
        return fluidId;
    }

    public int getLuminosity() {
        return luminosity;
    }

    public int getDensity() {
        return density;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getViscosity() {
        return viscosity;
    }

    public boolean isGaseous() {
        return gaseous;
    }

    public boolean hasNbt() {
        return !nbt.isEmpty();
    }

    public String getNbt() {
        return nbt;
    }

    @Override
    public int compareTo(Identifiable<String> other) {
        if (other instanceof Fluid) {
            return Comparator.comparing(Fluid::getModId)
                    .thenComparing(Fluid::getInternalName)
                    .thenComparing(Fluid::getNbt, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(Fluid::getId)
                    .compare(this, (Fluid) other);
        } else {
            return Identifiable.super.compareTo(other);
        }
    }
}
