package com.github.dcysteine.nesql.exporter.handler.forestry;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.toprettystring.ToPrettyString;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
public abstract class ChemicalReactorRecipe {
    public abstract Stack combInput();
    public abstract Stack otherItemInput();
    public abstract Optional<Stack> fluidInput();
    public abstract ImmutableSet<Stack> itemOutputs();
    public abstract Optional<Stack> fluidOutput();

    @ToPrettyString
    @Override
    public abstract String toString();

    public static Builder builder() {
        return new $AutoValue_ChemicalReactorRecipe.Builder();
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setCombInput(Stack combInput);
        public abstract Builder setOtherItemInput(Stack itemInput);
        public abstract Builder setFluidInput(@Nullable Stack fluidInput);

        abstract ImmutableSet.Builder<Stack> itemOutputsBuilder();
        public final Builder addItemOutput(Stack itemOutput) {
            itemOutputsBuilder().add(itemOutput);
            return this;
        }
        public final Builder addAllItemOutputs(Iterable<Stack> itemOutputs) {
            itemOutputsBuilder().addAll(itemOutputs);
            return this;
        }

        public abstract Builder setFluidOutput(@Nullable Stack fluidOutput);

        public abstract ChemicalReactorRecipe build();
    }
}
