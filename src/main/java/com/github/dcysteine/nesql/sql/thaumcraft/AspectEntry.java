package com.github.dcysteine.nesql.sql.thaumcraft;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;

/** Holds information about an {@link Item}'s quantity of a single aspect. */
@Entity
@EqualsAndHashCode
@ToString
public class AspectEntry implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Aspect aspect;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    private int amount;

    /** Needed by Hibernate. */
    protected AspectEntry() {}

    public AspectEntry(String id, Aspect aspect, Item item, int amount) {
        this.id = id;
        this.aspect = aspect;
        this.item = item;
        this.amount = amount;
    }

    @Override
    public String getId() {
        return id;
    }

    public Aspect getAspect() {
        return aspect;
    }

    public Item getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public int compareTo(Identifiable<String> other) {
        if (other instanceof AspectEntry) {
            return Comparator.comparing(AspectEntry::getAspect)
                    .thenComparing(AspectEntry::getAmount)
                    .thenComparing(AspectEntry::getId)
                    .compare(this, (AspectEntry) other);
        } else {
            return Identifiable.super.compareTo(other);
        }
    }
}
