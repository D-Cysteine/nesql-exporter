package com.github.dcysteine.nesql.exporter.handler.forestry;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.toprettystring.ToPrettyString;
import com.google.common.collect.ImmutableSet;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;

@AutoValue
public abstract class BeeSpecies {
    public abstract String name();
    public abstract EnumTemperature temperature();
    public abstract EnumHumidity humidity();
    public abstract ImmutableSet<Trait> traits();
    public abstract ImmutableSet<Stack> products();
    public abstract ImmutableSet<Stack> specialties();
    public abstract ImmutableSet<BeeBreedingRecipe> breedsFrom();
    public abstract ImmutableSet<BeeBreedingRecipe> breedsTo();

    @ToPrettyString
    @Override
    public abstract String toString();

    public static Builder builder() {
        return new $AutoValue_BeeSpecies.Builder();
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setName(String name);
        public abstract Builder setTemperature(EnumTemperature temperature);
        public abstract Builder setHumidity(EnumHumidity humidity);

        abstract ImmutableSet.Builder<Trait> traitsBuilder();
        public final Builder addTrait(Trait trait) {
            traitsBuilder().add(trait);
            return this;
        }
        public final Builder addAllTraits(Iterable<Trait> traits) {
            traitsBuilder().addAll(traits);
            return this;
        }

        abstract ImmutableSet.Builder<Stack> productsBuilder();
        public final Builder addProduct(Stack product) {
            productsBuilder().add(product);
            return this;
        }
        public final Builder addAllProducts(Iterable<Stack> products) {
            productsBuilder().addAll(products);
            return this;
        }

        abstract ImmutableSet.Builder<Stack> specialtiesBuilder();
        public final Builder addSpecialty(Stack specialty) {
            specialtiesBuilder().add(specialty);
            return this;
        }
        public final Builder addAllSpecialties(Iterable<Stack> specialties) {
            specialtiesBuilder().addAll(specialties);
            return this;
        }

        abstract ImmutableSet.Builder<BeeBreedingRecipe> breedsFromBuilder();
        public final Builder addBreedsFrom(BeeBreedingRecipe recipe) {
            breedsFromBuilder().add(recipe);
            return this;
        }
        public final Builder addAllBreedsFrom(Iterable<BeeBreedingRecipe> recipes) {
            breedsFromBuilder().addAll(recipes);
            return this;
        }

        abstract ImmutableSet.Builder<BeeBreedingRecipe> breedsToBuilder();
        public final Builder addBreedsTo(BeeBreedingRecipe recipe) {
            breedsToBuilder().add(recipe);
            return this;
        }
        public final Builder addAllBreedsTo(Iterable<BeeBreedingRecipe> recipes) {
            breedsToBuilder().addAll(recipes);
            return this;
        }

        public abstract BeeSpecies build();
    }
}