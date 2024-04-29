package com.github.dcysteine.nesql.sql.quest;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.fluid.FluidStack;
import com.github.dcysteine.nesql.sql.base.item.ItemGroup;
import com.github.dcysteine.nesql.sql.base.mob.Mob;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * This class contains data for the various types of BetterQuesting tasks.
 *
 * <p>Each field will only be set for certain task types. See {@link TaskType} for details.
 */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class Task implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    /** This field is always set. */
    @Column(nullable = false)
    private String name;

    /** This field is always set. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType type;

    @ManyToMany
    @OrderColumn
    private List<ItemGroup> items;

    @ElementCollection
    @OrderColumn
    private List<FluidStack> fluids;

    private boolean consume;

    @ManyToOne
    private Mob mob;

    private int numberRequired;

    @Column(nullable = false)
    private String dimensionName;

    /** Needed by Hibernate. */
    protected Task() {}

    public Task(
            String id, String name, TaskType type,
            List<ItemGroup> items, List<FluidStack> fluids,
            boolean consume, @Nullable Mob mob, int numberRequired, String dimensionName) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.items = items;
        this.fluids = fluids;
        this.consume = consume;
        this.mob = mob;
        this.numberRequired = numberRequired;
        this.dimensionName = dimensionName;
    }

    public Optional<Mob> getMob() {
        return Optional.ofNullable(mob);
    }
}