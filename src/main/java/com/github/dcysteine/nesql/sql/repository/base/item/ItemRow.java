package com.github.dcysteine.nesql.sql.repository.base.item;

import com.github.dcysteine.nesql.sql.util.IdUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "item")
@Table(name = "item")
public class ItemRow {
    /**
     * This is the unique table key, NOT the Minecraft item ID! The latter is not unique (there can
     * be multiple item rows for the same Minecraft item ID).
     */
    @Id
    private String id;

    @Column(nullable = false)
    private String imageFileName;

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

    /** Needed by Hibernate. */
    protected ItemRow() {}

    public ItemRow(ItemStack itemStack) {
        this.id = IdUtil.itemId(itemStack);
        this.imageFileName = this.id;
        this.unlocalizedName = itemStack.getUnlocalizedName();
        this.localizedName = itemStack.getDisplayName();
        this.itemId = Item.getIdFromItem(itemStack.getItem());
        this.itemDamage = itemStack.getItemDamage();
        this.nbt = itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : null;
    }

    /**
     * This is the unique table key, NOT the Minecraft item ID! The latter is not unique (there can
     * be multiple item rows for the same Minecraft item ID).
     */
    public String getId() {
        return id;
    }

    public String getImageFileName() {
        return imageFileName;
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

    // TODO if needed, add toString, hashCode, compareTo, etc.
}
