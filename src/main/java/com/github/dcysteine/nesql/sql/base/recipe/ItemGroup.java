package com.github.dcysteine.nesql.sql.base.recipe;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.Sql;
import com.github.dcysteine.nesql.sql.base.Item;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import java.util.Set;

/** A group of {@link ItemStack}s, all fitting into a single input slot in a recipe. */
@Entity
public class ItemGroup extends Identifiable<String> {
    @Id
    @Column(length = Sql.STRING_MAX_LENGTH)
    private String id;

    @ManyToMany(targetEntity = Item.class)
    private Set<ItemStack> itemStacks;

    /** Needed by Hibernate. */
    protected ItemGroup() {}

    public ItemGroup(String id, Set<ItemStack> itemStacks) {
        this.id = id;
        this.itemStacks = itemStacks;
    }

    @Override
    public String getId() {
        return id;
    }

    public Set<ItemStack> getItemStacks() {
        return itemStacks;
    }
}
