package com.github.dcysteine.nesql.sql.thaumcraft;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Comparator;

/** Holds information about an {@link Item}'s quantity of a single aspect. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class AspectEntry implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private Aspect aspect;

    @ManyToOne
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
