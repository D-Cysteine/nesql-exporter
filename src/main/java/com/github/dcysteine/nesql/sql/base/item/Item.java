package com.github.dcysteine.nesql.sql.base.item;

import com.github.dcysteine.nesql.sql.Identifiable;

import com.github.dcysteine.nesql.sql.Sql;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OrderColumn;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.List;

@Entity
@EqualsAndHashCode
@ToString
public class Item implements Identifiable<String> {
    /**
     * This is the unique table key, NOT the Minecraft item ID! The latter is not unique (there can
     * be multiple item rows for the same Minecraft item ID).
     */
    @Id
    @Column(length = Sql.STRING_MAX_LENGTH, nullable = false)
    private String id;

    @Column(length = Sql.STRING_MAX_LENGTH, nullable = false)
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
    private int itemId;

    private int itemDamage;

    @Nullable
    @Column(length = Sql.STRING_MAX_LENGTH)
    private String nbt;

    @ElementCollection
    @OrderColumn
    private List<String> tooltip;

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
            List<String> tooltip) {
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

    public boolean hasNbt() {
        return nbt != null;
    }

    @Nullable
    public String getNbt() {
        return nbt;
    }

    public List<String> getTooltip() {
        return tooltip;
    }
}
