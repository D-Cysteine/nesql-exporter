package com.github.dcysteine.nesql.exporter.handler.forestry;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.toprettystring.ToPrettyString;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
public abstract class Comb {
    public abstract String name();
    public abstract ImmutableSet<String> producedBy();
    public abstract ImmutableSet<String> specialtyOf();
    public abstract ImmutableSet<AutoclaveRecipe> autoclaveRecipes();
    public abstract Optional<CentrifugeRecipe> centrifugeRecipe();
    public abstract ImmutableSet<ChemicalReactorRecipe> chemicalReactorRecipes();

    @ToPrettyString
    @Override
    public abstract String toString();

    public static Builder builder() {
        return new $AutoValue_Comb.Builder();
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setName(String name);

        abstract ImmutableSet.Builder<String> producedByBuilder();
        public final Builder addProducedBy(String producedBy) {
            producedByBuilder().add(producedBy);
            return this;
        }
        public final Builder addAllProducedBy(Iterable<String> producedBy) {
            producedByBuilder().addAll(producedBy);
            return this;
        }

        abstract ImmutableSet.Builder<String> specialtyOfBuilder();
        public final Builder addSpecialtyOf(String specialtyOf) {
            specialtyOfBuilder().add(specialtyOf);
            return this;
        }
        public final Builder addAllSpecialtyOf(Iterable<String> specialtyOf) {
            specialtyOfBuilder().addAll(specialtyOf);
            return this;
        }

        abstract ImmutableSet.Builder<AutoclaveRecipe> autoclaveRecipesBuilder();
        public final Builder addAutoclaveRecipe(AutoclaveRecipe recipe) {
            autoclaveRecipesBuilder().add(recipe);
            return this;
        }
        public final Builder addAllAutoclaveRecipes(Iterable<AutoclaveRecipe> recipes) {
            autoclaveRecipesBuilder().addAll(recipes);
            return this;
        }

        public abstract Builder setCentrifugeRecipe(@Nullable CentrifugeRecipe recipe);

        abstract ImmutableSet.Builder<ChemicalReactorRecipe> chemicalReactorRecipesBuilder();
        public final Builder addChemicalReactorRecipe(ChemicalReactorRecipe recipe) {
            chemicalReactorRecipesBuilder().add(recipe);
            return this;
        }
        public final Builder addAllChemicalReactorRecipes(Iterable<ChemicalReactorRecipe> recipes) {
            chemicalReactorRecipesBuilder().addAll(recipes);
            return this;
        }

        public abstract Comb build();
    }
}
