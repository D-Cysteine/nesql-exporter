package com.github.dcysteine.nesql.sql.base.entity;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.Metadata;
import com.github.dcysteine.nesql.sql.quest.Task;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.List;

@jakarta.persistence.Entity
@EqualsAndHashCode
@ToString
public class Entity implements Identifiable<String> {
    /**
     * This is the unique table key, NOT the Minecraft item ID! The latter is not unique (there can
     * be multiple item rows for the same Minecraft item ID).
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

    /** The Minecraft entity ID. These IDs can vary from world to world, so don't rely on them! */
    private int entityId;

    @Column(length = Metadata.MAX_STRING_LENGTH, nullable = false)
    private String nbt;

    @OneToMany
    @OrderColumn
    private List<Task> tasks;

    /** Needed by Hibernate. */
    protected Entity() {}

    public Entity(
            String id,
            String imageFilePath,
            String modId,
            String internalName,
            String unlocalizedName,
            String localizedName,
            int entityId,
            String nbt) {
        this.id = id;
        this.imageFilePath = imageFilePath;
        this.modId = modId;
        this.internalName = internalName;
        this.unlocalizedName = unlocalizedName;
        this.localizedName = localizedName;
        this.entityId = entityId;
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

    public String getModId() {
        return modId;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getUnlocalizedName() { return unlocalizedName; }

    public String getLocalizedName() {
        return localizedName;
    }

    /** The Minecraft item ID. These IDs can vary from world to world, so don't rely on them! */
    public int getEntityId() {
        return entityId;
    }

    public boolean hasNbt() {
        return !nbt.isEmpty();
    }

    public String getNbt() {
        return nbt;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public int compareTo(Identifiable<String> other) {
        if (other instanceof Entity) {
            return Comparator.comparing(Entity::getModId)
                    .thenComparing(Entity::getInternalName)
                    .thenComparing(Entity::getUnlocalizedName)
                    .thenComparing(Entity::getNbt, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(Entity::getId)
                    .compare(this, (Entity) other);
        } else {
            return Identifiable.super.compareTo(other);
        }
    }
}