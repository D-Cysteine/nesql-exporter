package com.github.dcysteine.nesql.sql.forge;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@EqualsAndHashCode
@ToString
public class OreDictionary implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @OneToOne
    private ItemGroup itemGroup;

    /** Needed by Hibernate. */
    protected OreDictionary() {}

    public OreDictionary(String id, String name, ItemGroup itemGroup) {
        this.id = id;
        this.name = name;
        this.itemGroup = itemGroup;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ItemGroup getItemGroup() {
        return itemGroup;
    }
}
