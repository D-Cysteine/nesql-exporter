package com.github.dcysteine.nesql.exporter.handler.forestry;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.toprettystring.ToPrettyString;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

@AutoValue
public abstract class Data {
    public static Data create(Set<BeeSpecies> beeSpecies, Set<Comb> combs) {
        return new AutoValue_Data(ImmutableSet.copyOf(beeSpecies), ImmutableSet.copyOf(combs));
    }

    public abstract ImmutableSet<BeeSpecies> beeSpecies();
    public abstract ImmutableSet<Comb> combs();

    @ToPrettyString
    @Override
    public abstract String toString();
}
