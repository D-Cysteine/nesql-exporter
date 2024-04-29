package com.github.dcysteine.nesql.sql.base.item;

import com.github.dcysteine.nesql.sql.Identifiable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/** A group of {@link ItemStack}s, all fitting into a single input slot in a recipe. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class ItemGroup implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ElementCollection
    private Set<ItemStack> itemStacks;

    /**
     * The item group that has the same item stacks as this one, but with all stack sizes set to 1.
     * May be the same item group as this one! Used to find ore dictionary associations.
     */
    @EqualsAndHashCode.Exclude
    @Setter
    @ManyToOne
    private ItemGroup baseItemGroup;

    /** Needed by Hibernate. */
    protected ItemGroup() {}

    public ItemGroup(String id, Set<ItemStack> itemStacks) {
        this.id = id;
        this.itemStacks = itemStacks;
        this.baseItemGroup = this;
    }
}