package com.github.dcysteine.nesql.sql.mobinfo;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.mob.Mob;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.SortNatural;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
@EqualsAndHashCode
@Getter
@ToString
public class MobInfo implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @OneToOne
    Mob mob;

    @ElementCollection
    @SortNatural
    private SortedSet<MobDrop> drops;

    private boolean allowedInPeaceful;
    private boolean soulVialUsable;
    private boolean allowedInfernal;
    private boolean alwaysInfernal;

    @ElementCollection
    @SortNatural
    private SortedSet<String> spawnInfo;

    /** Needed by Hibernate. */
    protected MobInfo() {}

    public MobInfo(
            String id, Mob mob, boolean allowedInPeaceful, boolean soulVialUsable,
            boolean allowedInfernal, boolean alwaysInfernal, Collection<String> spawnInfo) {
        this.id = id;
        this.mob = mob;
        this.allowedInPeaceful = allowedInPeaceful;
        this.soulVialUsable = soulVialUsable;
        this.allowedInfernal = allowedInfernal;
        this.alwaysInfernal = alwaysInfernal;
        this.spawnInfo = new TreeSet<>(spawnInfo);

        drops = new TreeSet<>();
    }

    public void addDrop(MobDrop drop) {
        drops.add(drop);
    }

    @Override
    public int compareTo(Identifiable<String> other) {
        if (other instanceof MobInfo) {
            return Comparator.comparing(MobInfo::getMob)
                    .thenComparing(MobInfo::getId)
                    .compare(this, (MobInfo) other);
        } else {
            return Identifiable.super.compareTo(other);
        }
    }
}
