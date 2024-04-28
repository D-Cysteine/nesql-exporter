package com.github.dcysteine.nesql.sql.base.mob;

import com.github.dcysteine.nesql.sql.Identifiable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;

@Entity
@EqualsAndHashCode
@ToString
public class Mob implements Identifiable<String> {
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
    private String localizedName;

    private double width;
    private double height;
    private double health;
    private int armour;
    private boolean immuneToFire;
    private boolean leashable;

    /** Needed by Hibernate. */
    protected Mob() {}

    public Mob(
            String id,
            String imageFilePath,
            String modId,
            String internalName,
            String localizedName,
            double width,
            double height,
            double health,
            int armour,
            boolean immuneToFire,
            boolean leashable) {
        this.id = id;
        this.imageFilePath = imageFilePath;
        this.modId = modId;
        this.internalName = internalName;
        this.localizedName = localizedName;
        this.width = width;
        this.height = height;
        this.health = health;
        this.armour = armour;
        this.immuneToFire = immuneToFire;
        this.leashable = leashable;
    }

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

    public String getLocalizedName() {
        return localizedName;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getHealth() {
        return health;
    }

    public int getArmour() {
        return armour;
    }

    public boolean isImmuneToFire() {
        return immuneToFire;
    }

    public boolean isLeashable() {
        return leashable;
    }

    @Override
    public int compareTo(Identifiable<String> other) {
        if (other instanceof Mob) {
            return Comparator.comparing(Mob::getModId)
                    .thenComparing(Mob::getInternalName)
                    .thenComparing(Mob::getId)
                    .compare(this, (Mob) other);
        } else {
            return Identifiable.super.compareTo(other);
        }
    }
}
