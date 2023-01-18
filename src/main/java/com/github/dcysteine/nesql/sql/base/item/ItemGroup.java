package com.github.dcysteine.nesql.sql.base.item;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.Sql;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SortNatural;

import java.util.SortedSet;

/** A group of {@link ItemStack}s, all fitting into a single input slot in a recipe. */
@Entity
@EqualsAndHashCode
@ToString
public class ItemGroup implements Identifiable<String> {
    @Id
    @Column(length = Sql.STRING_MAX_LENGTH, nullable = false)
    private String id;

    @ElementCollection
    @SortNatural
    private SortedSet<ItemStack> itemStacks;

    @ElementCollection
    @SortNatural
    private SortedSet<WildcardItemStack> wildcardItemStacks;

    /** Needed by Hibernate. */
    protected ItemGroup() {}

    public ItemGroup(
            String id,
            SortedSet<ItemStack> itemStacks,
            SortedSet<WildcardItemStack> wildcardItemStacks) {
        this.id = id;
        this.itemStacks = itemStacks;
        this.wildcardItemStacks = wildcardItemStacks;
    }

    @Override
    public String getId() {
        return id;
    }

    public SortedSet<ItemStack> getItemStacks() {
        return itemStacks;
    }

    public SortedSet<WildcardItemStack> getWildcardItemStacks() {
        return wildcardItemStacks;
    }
}