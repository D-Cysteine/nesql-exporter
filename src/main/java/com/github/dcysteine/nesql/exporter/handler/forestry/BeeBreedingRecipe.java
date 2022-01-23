package com.github.dcysteine.nesql.exporter.handler.forestry;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.toprettystring.ToPrettyString;
import com.google.common.collect.ImmutableSet;

@AutoValue
public abstract class BeeBreedingRecipe {
    public static BeeBreedingRecipe create(String parent1, String parent2, String child) {
        return new AutoValue_BeeBreedingRecipe(ImmutableSet.of(parent1, parent2), child);
    }

    public abstract ImmutableSet<String> parents();
    public abstract String child();

    @ToPrettyString
    @Override
    public abstract String toString();
}
