package com.github.dcysteine.nesql.exporter.handler.forestry;

import com.google.common.base.Preconditions;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IChromosome;
import forestry.api.genetics.IMutation;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ForestryReader {
    private SetMultimap<String, BeeBreedingRecipe> breedsFromRecipes;
    private SetMultimap<String, BeeBreedingRecipe> breedsToRecipes;
    public Set<BeeSpecies> beeSpecies;

    private SetMultimap<ItemStackWrapper, String> combsProducedBy;
    private SetMultimap<ItemStackWrapper, String> combsSpecialtyOf;
    private SetMultimap<ItemStackWrapper, AutoclaveRecipe> autoclaveRecipes;
    private Map<ItemStackWrapper, CentrifugeRecipe> centrifugeRecipes;
    private SetMultimap<ItemStackWrapper, ChemicalReactorRecipe> chemicalReactorRecipes;
    public Set<Comb> combs;

    public ForestryReader() {
        this.breedsFromRecipes = MultimapBuilder.hashKeys().hashSetValues().build();
        this.breedsToRecipes = MultimapBuilder.hashKeys().hashSetValues().build();
        this.beeSpecies = new HashSet<>();

        this.combsProducedBy = MultimapBuilder.hashKeys().hashSetValues().build();
        this.combsSpecialtyOf = MultimapBuilder.hashKeys().hashSetValues().build();
        this.centrifugeRecipes = new HashMap<>();
        this.autoclaveRecipes = MultimapBuilder.hashKeys().hashSetValues().build();
        this.chemicalReactorRecipes = MultimapBuilder.hashKeys().hashSetValues().build();
        this.combs = new HashSet<>();
    }

    public void read() {
        IBeeRoot rootBee = (IBeeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootBees");
        Preconditions.checkNotNull(rootBee);

        for (IMutation mutation : rootBee.getMutations(false)) {
            processMutation(mutation);
        }

        for (IBee bee : rootBee.getIndividualTemplates()) {
            processBeeGenome(bee.getGenome());
        }

        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes.mRecipeList) {
            processAutoclaveRecipe(recipe);
        }

        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.mRecipeList) {
            processCentrifugeRecipe(recipe);
        }

        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sChemicalRecipes.mRecipeList) {
            processChemicalReactorRecipe(recipe);
        }

        for (ItemStackWrapper comb
                : Sets.union(combsProducedBy.keySet(), combsSpecialtyOf.keySet())) {
            processComb(comb);
        }
    }

    private static boolean isComb(ItemStack itemStack) {
        return itemStack.getDisplayName().endsWith("Comb");
    }

    private static float buildGTChance(int chance) {
        return chance / 100.0f;
    }

    private void processMutation(IMutation mutation) {
        String parent1 = mutation.getAllele0().getName();
        String parent2 = mutation.getAllele1().getName();
        String child = mutation.getTemplate()[EnumBeeChromosome.SPECIES.ordinal()].getName();
        BeeBreedingRecipe recipe = BeeBreedingRecipe.create(parent1, parent2, child);

        breedsFromRecipes.put(child, recipe);
        breedsToRecipes.put(parent1, recipe);
        breedsToRecipes.put(parent2, recipe);
    }

    private void processBeeGenome(IBeeGenome genome) {
        IAlleleBeeSpecies species = genome.getPrimary();
        String name = species.getName();
        BeeSpecies.Builder builder =
                BeeSpecies.builder()
                        .setName(name)
                        .setTemperature(species.getTemperature())
                        .setHumidity(species.getHumidity())
                        .addAllBreedsFrom(breedsFromRecipes.get(name))
                        .addAllBreedsTo(breedsToRecipes.get(name));

        IChromosome[] chromosomes = genome.getChromosomes();
        for (EnumBeeChromosome chromosomeType : EnumBeeChromosome.values()) {
            if (chromosomeType == EnumBeeChromosome.HUMIDITY) {
                // HUMIDITY is deprecated and no allele will be present for it.
                continue;
            }

            IAllele allele = genome.getActiveAllele(chromosomeType);
            Trait.Dominance dominance =
                    allele.isDominant() ? Trait.Dominance.DOMINANT : Trait.Dominance.RECESSIVE;

            builder.addTrait(Trait.create(chromosomeType, allele.getName(), dominance));
        }

        for (Map.Entry<ItemStack, Float> entry : species.getProductChances().entrySet()) {
            ItemStack itemStack = entry.getKey();
            builder.addProduct(Stack.create(itemStack, entry.getValue()));

            if (isComb(itemStack)) {
                combsProducedBy.put(ItemStackWrapper.create(itemStack), name);
            }
        }

        for (Map.Entry<ItemStack, Float> entry : species.getSpecialtyChances().entrySet()) {
            ItemStack itemStack = entry.getKey();
            builder.addSpecialty(Stack.create(itemStack, entry.getValue()));

            if (isComb(itemStack)) {
                combsSpecialtyOf.put(ItemStackWrapper.create(itemStack), name);
            }
        }

        beeSpecies.add(builder.build());
    }

    private void processAutoclaveRecipe(GT_Recipe recipe) {
        ItemStack firstItemInput = recipe.mInputs[0];
        if (isComb(firstItemInput)) {
            Preconditions.checkState(
                    recipe.mInputs[1].getItem() == GT_Utility.getIntegratedCircuit(0).getItem());

            AutoclaveRecipe.Builder builder =
                    AutoclaveRecipe.builder()
                            .setCombInput(Stack.create(firstItemInput))
                            .setFluidInput(Stack.create(recipe.mFluidInputs[0]));

            for (int i = 0; i < recipe.mOutputs.length; i++) {
                ItemStack output = recipe.mOutputs[i];
                if (output == null) {
                    continue;
                }

                float chance = buildGTChance(recipe.getOutputChance(i));
                builder.addItemOutput(Stack.create(output, chance));
            }

            if (recipe.mFluidOutputs.length > 0) {
                builder.setFluidOutput(Stack.create(recipe.mFluidOutputs[0]));
            }

            autoclaveRecipes.put(ItemStackWrapper.create(firstItemInput), builder.build());
        } else if (recipe.mInputs.length > 1) {
            ItemStack secondItemInput = recipe.mInputs[1];
            Preconditions.checkState(secondItemInput == null || !isComb(secondItemInput));
        }
    }

    private void processCentrifugeRecipe(GT_Recipe recipe) {
        if (recipe.mInputs.length < 1) {
            return;
        }

        ItemStack firstItemInput = recipe.mInputs[0];
        if (isComb(firstItemInput)) {
            Preconditions.checkState(recipe.mInputs.length == 1);
            Preconditions.checkState(recipe.mFluidInputs.length == 0);

            CentrifugeRecipe.Builder builder =
                    CentrifugeRecipe.builder().setCombInput(Stack.create(firstItemInput));

            for (int i = 0; i < recipe.mOutputs.length; i++) {
                ItemStack output = recipe.mOutputs[i];
                if (output == null) {
                    continue;
                }

                float chance = buildGTChance(recipe.getOutputChance(i));
                builder.addItemOutput(Stack.create(output, chance));
            }

            if (recipe.mFluidOutputs.length > 0) {
                builder.setFluidOutput(Stack.create(recipe.mFluidOutputs[0]));
            }

            ItemStackWrapper comb = ItemStackWrapper.create(firstItemInput);
            Preconditions.checkState(!centrifugeRecipes.containsKey(comb));
            centrifugeRecipes.put(comb, builder.build());
        } else if (recipe.mInputs.length > 1) {
            ItemStack secondItemInput = recipe.mInputs[1];
            Preconditions.checkState(secondItemInput == null || !isComb(secondItemInput));
        }
    }

    private void processChemicalReactorRecipe(GT_Recipe recipe) {
        ItemStack firstItemInput = recipe.mInputs[0];
        if (isComb(firstItemInput)) {
            ChemicalReactorRecipe.Builder builder =
                    ChemicalReactorRecipe.builder()
                            .setCombInput(Stack.create(firstItemInput))
                            .setOtherItemInput(Stack.create(recipe.mInputs[1]));

            if (recipe.mFluidInputs.length > 0) {
                builder.setFluidInput(Stack.create(recipe.mFluidInputs[0]));
            }

            for (int i = 0; i < recipe.mOutputs.length; i++) {
                ItemStack output = recipe.mOutputs[i];
                if (output == null) {
                    continue;
                }

                float chance = buildGTChance(recipe.getOutputChance(i));
                builder.addItemOutput(Stack.create(output, chance));
            }

            if (recipe.mFluidOutputs.length > 0) {
                builder.setFluidOutput(Stack.create(recipe.mFluidOutputs[0]));
            }

            chemicalReactorRecipes.put(ItemStackWrapper.create(firstItemInput), builder.build());
        } else if (recipe.mInputs.length > 1) {
            ItemStack secondItemInput = recipe.mInputs[1];
            Preconditions.checkState(secondItemInput == null || !isComb(secondItemInput));
        }
    }

    private void processComb(ItemStackWrapper comb) {
        Comb.Builder builder =
                Comb.builder()
                        .setName(comb.name())
                        .addAllProducedBy(combsProducedBy.get(comb))
                        .addAllSpecialtyOf(combsSpecialtyOf.get(comb))
                        .addAllAutoclaveRecipes(autoclaveRecipes.get(comb))
                        .addAllChemicalReactorRecipes(chemicalReactorRecipes.get(comb));

        if (centrifugeRecipes.containsKey(comb)) {
            builder.setCentrifugeRecipe(centrifugeRecipes.get(comb));
        }

        combs.add(builder.build());
    }
}
