package com.github.dcysteine.nesql.sql.base.item;

import com.github.dcysteine.nesql.sql.Identifiable;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import javax.annotation.Nullable;

@Entity
@Table
public class Item implements Identifiable<String> {
    /**
     * This is the unique table key, NOT the Minecraft item ID! The latter is not unique (there can
     * be multiple item rows for the same Minecraft item ID).
     */
    @Id
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

    /**
     * The Minecraft item ID.
     */
    @Column
    private int itemId;

    @Column
    private int itemDamage;

    // Hmm, I wonder if this is long enough?
    @Column(length = 2048)
    private String nbt;

    @Column(nullable = false)
    private String tooltip;

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
            @Nullable String nbt,
            String tooltip) {
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

    /** The Minecraft item ID. */
    public int getItemId() {
        return itemId;
    }

    public int getItemDamage() {
        return itemDamage;
    }

    @Nullable
    public String getNbt() {
        return nbt;
    }

    public String getTooltip() {
        return tooltip;
    }

    // TODO if needed, add toString, hashCode, compareTo, etc.
}
