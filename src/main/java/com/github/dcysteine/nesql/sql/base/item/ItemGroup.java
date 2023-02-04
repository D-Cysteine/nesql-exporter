package com.github.dcysteine.nesql.sql.base.item;

import com.github.dcysteine.nesql.sql.Identifiable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/** A group of {@link ItemStack}s, all fitting into a single input slot in a recipe. */
@Entity
@EqualsAndHashCode
@ToString
public class ItemGroup implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ElementCollection
    @CollectionTable(indexes = {@Index(columnList = "ITEM_STACKS_ITEM_ID")})
    private Set<ItemStack> itemStacks;

    @ElementCollection
    private Set<WildcardItemStack> wildcardItemStacks;

    /** We resolve wildcard item groups, to speed up queries. */
    @ElementCollection
    @CollectionTable(indexes = {@Index(columnList = "RESOLVED_WILDCARD_ITEM_STACKS_ITEM_ID")})
    private Set<ItemStack> resolvedWildcardItemStacks;

    /** Needed by Hibernate. */
    protected ItemGroup() {}

    public ItemGroup(
            String id,
            Set<ItemStack> itemStacks,
            Set<WildcardItemStack> wildcardItemStacks) {
        this.id = id;
        this.itemStacks = itemStacks;
        this.wildcardItemStacks = wildcardItemStacks;
        this.resolvedWildcardItemStacks = new HashSet<>();
    }

    @Override
    public String getId() {
        return id;
    }

    public Set<ItemStack> getItemStacks() {
        return itemStacks;
    }

    public Set<WildcardItemStack> getWildcardItemStacks() {
        return wildcardItemStacks;
    }

    public Set<ItemStack> getResolvedWildcardItemStacks() {
        return resolvedWildcardItemStacks;
    }

    public void addAllResolvedWildcardItemStacks(Collection<ItemStack> itemStacks) {
        this.resolvedWildcardItemStacks.addAll(itemStacks);
    }
}