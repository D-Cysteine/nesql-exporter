package com.github.dcysteine.nesql.sql.base.item;

import com.github.dcysteine.nesql.sql.Identifiable;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.Map;

@Entity
@Table(indexes = {@Index(columnList = "itemId")})
@EqualsAndHashCode
@ToString
public class Item implements Identifiable<String> {
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

    /** The Minecraft item ID. These IDs can vary from world to world, so don't rely on them! */
    private int itemId;

    private int itemDamage;

    @Lob
    @Column(nullable = false)
    private String nbt;

    @Lob
    @Column(nullable = false)
    private String tooltip;

    private int maxStackSize;

    private int maxDamage;

    /** Map of tool class to harvest level. */
    @ElementCollection
    private Map<String, Integer> toolClasses;

    /** Needed by Hibernate. */
    protected Item() {}

    public Item(
            String id,
            String imageFilePath,
            String modId,
            String internalName,
            String unlocalizedName,
            String localizedName,
            int itemId,
            int itemDamage,
            String nbt,
            String tooltip,
            int maxStackSize,
            int maxDamage,
            Map<String, Integer> toolClasses) {
        this.id = id;
        this.imageFilePath = imageFilePath;
        this.modId = modId;
        this.internalName = internalName;
        this.unlocalizedName = unlocalizedName;
        this.localizedName = localizedName;
        this.itemId = itemId;
        this.itemDamage = itemDamage;
        this.nbt = nbt;
        this.tooltip = tooltip;
        this.maxStackSize = maxStackSize;
        this.maxDamage = maxDamage;
        this.toolClasses = toolClasses;
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

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    /** The Minecraft item ID. These IDs can vary from world to world, so don't rely on them! */
    public int getItemId() {
        return itemId;
    }

    public int getItemDamage() {
        return itemDamage;
    }

    public boolean hasNbt() {
        return !nbt.isEmpty();
    }

    public String getNbt() {
        return nbt;
    }

    public String getTooltip() {
        return tooltip;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    /** Returns a map of tool class to harvest level. */
    public Map<String, Integer> getToolClasses() {
        return toolClasses;
    }

    @Override
    public int compareTo(Identifiable<String> other) {
        if (other instanceof Item) {
            return Comparator.comparing(Item::getModId)
                    .thenComparing(Item::getInternalName)
                    .thenComparing(Item::getItemDamage)
                    .thenComparing(Item::getNbt, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(Item::getId)
                    .compare(this, (Item) other);
        } else {
            return Identifiable.super.compareTo(other);
        }
    }
}