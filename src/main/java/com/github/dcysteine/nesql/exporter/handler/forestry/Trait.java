package com.github.dcysteine.nesql.exporter.handler.forestry;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.toprettystring.ToPrettyString;
import forestry.api.apiculture.EnumBeeChromosome;

@AutoValue
public abstract class Trait {
    public static Trait create(EnumBeeChromosome type, String name, Dominance dominance) {
        return new AutoValue_Trait(type, name, dominance);
    }

    public enum Dominance {
        DOMINANT, RECESSIVE;
    }

    public abstract EnumBeeChromosome type();
    public abstract String name();
    public abstract Dominance dominance();

    @ToPrettyString
    @Override
    public abstract String toString();
}
