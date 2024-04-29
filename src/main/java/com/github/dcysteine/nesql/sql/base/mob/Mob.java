package com.github.dcysteine.nesql.sql.base.mob;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.Metadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Comparator;

@Entity
@EqualsAndHashCode
@Getter
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

    @Column(length = Metadata.MAX_STRING_LENGTH, nullable = false)
    private String nbt;

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
            String nbt,
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
        this.nbt = nbt;
        this.width = width;
        this.height = height;
        this.health = health;
        this.armour = armour;
        this.immuneToFire = immuneToFire;
        this.leashable = leashable;
    }

    public boolean hasNbt() {
        return !nbt.isEmpty();
    }

    @Override
    public int compareTo(Identifiable<String> other) {
        if (other instanceof Mob) {
            return Comparator.comparing(Mob::getModId)
                    .thenComparing(Mob::getInternalName)
                    .thenComparing(Mob::getNbt, Comparator.naturalOrder())
                    .thenComparing(Mob::getId)
                    .compare(this, (Mob) other);
        } else {
            return Identifiable.super.compareTo(other);
        }
    }
}
