package com.github.dcysteine.nesql.sql.base.item;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.Sql;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SortNatural;

import java.util.Set;

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
    private Set<ItemStack> itemStacks;

    @ManyToMany
    @SortNatural
    private Set<WildcardItem> wildcardItems;

    /** Needed by Hibernate. */
    protected ItemGroup() {}

    public ItemGroup(String id, Set<ItemStack> itemStacks, Set<WildcardItem> wildcardItems) {
        this.id = id;
        this.itemStacks = itemStacks;
        this.wildcardItems = wildcardItems;
    }

    @Override
    public String getId() {
        return id;
    }

    public Set<ItemStack> getItemStacks() {
        return itemStacks;
    }

    public Set<WildcardItem> getWildcardItems() {
        return wildcardItems;
    }
}
