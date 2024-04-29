package com.github.dcysteine.nesql.exporter.plugin.gregtech.util;

import com.github.dcysteine.nesql.sql.base.recipe.Dimension;
import gregtech.api.enums.ItemList;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_LanguageManager;

/** Enum of supported GregTech recipe maps. */
public enum RecipeMap {
    ORE_WASHER(
            RecipeMaps.oreWasherRecipes,
            "orewasher",
            ItemList.Machine_HV_OreWasher,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(3, 1),
            new Dimension(0, 0)),

    THERMAL_CENTRIFUGE(
            RecipeMaps.thermalCentrifugeRecipes,
            "thermalcentrifuge",
            ItemList.Machine_HV_ThermalCentrifuge,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(3, 1),
            new Dimension(0, 0)),

    COMPRESSOR(
            RecipeMaps.compressorRecipes,
            "compressor",
            ItemList.Machine_HV_Compressor,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    EXTRACTOR(
            RecipeMaps.extractorRecipes,
            "extractor",
            ItemList.Machine_HV_Extractor,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    SCANNER(
            RecipeMaps.scannerFakeRecipes,
            "scanner",
            ItemList.Machine_HV_Scanner,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    ROCK_BREAKER(
            RecipeMaps.rockBreakerFakeRecipes,
            "rockbreaker",
            ItemList.Machine_HV_RockBreaker,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    REPLICATOR(
            RecipeMaps.replicatorRecipes,
            "replicator",
            ItemList.Machine_HV_Replicator,
            true,
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    ASSEMBLY_LINE(
            RecipeMaps.assemblylineVisualRecipes,
            "assemblyline",
            ItemList.Machine_Multi_Assemblyline,
            false,
            new Dimension(4, 4),
            new Dimension(4, 1),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    PLASMA_ARC_FURNACE(
            RecipeMaps.plasmaArcFurnaceRecipes,
            "plasmaarcfurnace",
            ItemList.Machine_HV_PlasmaArcFurnace,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(2, 2),
            new Dimension(1, 1)),

    ARC_FURNACE(
            RecipeMaps.arcFurnaceRecipes,
            "arcfurnace",
            ItemList.Machine_HV_ArcFurnace,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(2, 2),
            new Dimension(0, 0)),

    PRINTER(
            RecipeMaps.printerRecipes,
            "printer",
            ItemList.Machine_HV_Printer,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    SIFTER(
            RecipeMaps.sifterRecipes,
            "sifter",
            ItemList.Machine_HV_Sifter,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(3, 3),
            new Dimension(1, 1)),

    FORMING_PRESS(
            RecipeMaps.formingPressRecipes,
            "formingpress",
            ItemList.Machine_HV_Press,
            true,
            new Dimension(3, 2),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    LASER_ENGRAVER(
            RecipeMaps.laserEngraverRecipes,
            "laserengraver",
            ItemList.Machine_HV_LaserEngraver,
            true,
            new Dimension(2, 2),
            new Dimension(2, 1),
            new Dimension(2, 2),
            new Dimension(2, 1)),

    MIXER(
            RecipeMaps.mixerRecipes,
            "mixer",
            ItemList.Machine_HV_Mixer,
            true,
            new Dimension(3, 3),
            new Dimension(1, 1),
            new Dimension(2, 2),
            new Dimension(1, 1)),

    AUTOCLAVE(
            RecipeMaps.autoclaveRecipes,
            "autoclave",
            ItemList.Machine_HV_Autoclave,
            true,
            new Dimension(2, 1),
            new Dimension(1, 1),
            new Dimension(2, 2),
            new Dimension(1, 1)),

    ELECTROMAGNETIC_SEPARATOR(
            RecipeMaps.electroMagneticSeparatorRecipes,
            "electromagneticseparator",
            ItemList.Machine_HV_ElectromagneticSeparator,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(3, 1),
            new Dimension(0, 0)),

    POLARIZER(
            RecipeMaps.polarizerRecipes,
            "polarizer",
            ItemList.Machine_HV_Polarizer,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    MACERATOR(
            RecipeMaps.maceratorRecipes,
            "macerator",
            ItemList.Machine_HV_Macerator,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(2, 2),
            new Dimension(0, 0)),

    CHEMICAL_BATH(
            RecipeMaps.chemicalBathRecipes,
            "chemicalbath",
            ItemList.Machine_HV_ChemicalBath,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(3, 1),
            new Dimension(1, 1)),

    FLUID_CANNER(
            RecipeMaps.fluidCannerRecipes,
            "fluidcanner",
            ItemList.Machine_HV_FluidCanner,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(1, 1)),

    BREWING(
            RecipeMaps.brewingRecipes,
            "brewing",
            ItemList.Machine_HV_Brewery,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(1, 1)),

    FLUID_HEATER(
            RecipeMaps.fluidHeaterRecipes,
            "fluidheater",
            ItemList.Machine_HV_FluidHeater,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(1, 1)),

    DISTILLERY(
            RecipeMaps.distilleryRecipes,
            "distillery",
            ItemList.Machine_HV_Distillery,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(1, 1)),

    FERMENTING(
            RecipeMaps.fermentingRecipes,
            "fermenting",
            ItemList.Machine_HV_Fermenter,
            true,
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(1, 1)),

    FLUID_SOLIDIFIER(
            RecipeMaps.fluidSolidifierRecipes,
            "fluidsolidifier",
            ItemList.Machine_HV_FluidSolidifier,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    FLUID_EXTRACTION(
            RecipeMaps.fluidExtractionRecipes,
            "fluidextraction",
            ItemList.Machine_HV_FluidExtractor,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(1, 1)),

    PACKAGER(
            RecipeMaps.packagerRecipes,
            "packager",
            ItemList.Machine_HV_Boxinator,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    UNPACKAGER(
            RecipeMaps.unpackagerRecipes,
            "unpackager",
            ItemList.Machine_HV_Unboxinator,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(2, 1),
            new Dimension(0, 0)),

    FUSION(
            RecipeMaps.fusionRecipes,
            "fusion",
            ItemList.FusionComputer_LuV,
            true,
            new Dimension(0, 0),
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(1, 1)),

    CENTRIFUGE(
            RecipeMaps.centrifugeRecipes,
            "centrifuge",
            ItemList.Machine_HV_Centrifuge,
            true,
            new Dimension(2, 1),
            new Dimension(1, 1),
            new Dimension(3, 2),
            new Dimension(1, 1)),

    ELECTROLYZER(
            RecipeMaps.electrolyzerRecipes,
            "electrolyzer",
            ItemList.Machine_HV_Electrolyzer,
            true,
            new Dimension(2, 1),
            new Dimension(1, 1),
            new Dimension(3, 2),
            new Dimension(1, 1)),

    BLAST_FURNACE(
            RecipeMaps.blastFurnaceRecipes,
            "blastfurnace",
            ItemList.Machine_Multi_BlastFurnace,
            true,
            new Dimension(3, 2),
            new Dimension(1, 1),
            new Dimension(3, 2),
            new Dimension(1, 1)),

    PLASMA_FORGE(
            RecipeMaps.plasmaForgeRecipes,
            "plasmaforge",
            ItemList.Machine_Multi_PlasmaForge,
            true,
            new Dimension(3, 3),
            new Dimension(3, 3),
            new Dimension(3, 3),
            new Dimension(3, 3)),

    TRANSCENDENT_PLASMA_MIXER(
            RecipeMaps.transcendentPlasmaMixerRecipes,
            "transcendentplasmaforge",
            ItemList.Machine_Multi_TranscendentPlasmaMixer,
            true,
            new Dimension(1, 1),
            new Dimension(4, 5),
            new Dimension(0, 0),
            new Dimension(1, 1)),

    // TODO RecipeMaps.spaceProjectFakeRecipes
    // Also other space elevator recipes? Eye of Harmony? Don't seem to be in RecipeMaps though
    // gregtech.api.recipe.RecipeMap.ALL_RECIPE_MAPS.get("gt.recipe.<recipe>") ?

    PRIMITIVE_BLAST(
            RecipeMaps.primitiveBlastRecipes,
            "primitiveblast",
            ItemList.Machine_Bricked_BlastFurnace,
            true,
            new Dimension(3, 1),
            new Dimension(0, 0),
            new Dimension(3, 1),
            new Dimension(0, 0)),

    IMPLOSION(
            RecipeMaps.implosionRecipes,
            "implosion",
            ItemList.Machine_Multi_ImplosionCompressor,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(2, 1),
            new Dimension(0, 0)),

    VACUUM_FREEZER(
            RecipeMaps.vacuumFreezerRecipes,
            "vacuumfreezer",
            ItemList.Machine_Multi_VacuumFreezer,
            true,
            new Dimension(1, 1),
            new Dimension(2, 1),
            new Dimension(1, 1),
            new Dimension(1, 1)),

    CHEMICAL_REACTOR(
            RecipeMaps.chemicalReactorRecipes,
            "chemicalreactor",
            ItemList.Machine_HV_ChemicalReactor,
            true,
            new Dimension(2, 1),
            new Dimension(1, 1),
            new Dimension(2, 1),
            new Dimension(1, 1)),

    MULTIBLOCK_CHEMICAL_REACTOR(
            RecipeMaps.multiblockChemicalReactorRecipes,
            "multiblockchemicalReactor",
            ItemList.Machine_Multi_LargeChemicalReactor,
            true,
            new Dimension(3, 2),
            new Dimension(3, 2),
            new Dimension(3, 2),
            new Dimension(3, 2)),

    DISTILLATION_TOWER(
            RecipeMaps.distillationTowerRecipes,
            "distillationtower",
            ItemList.Distillation_Tower,
            true,
            new Dimension(2, 1),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(3, 4)),

    CRACKING(
            RecipeMaps.crackingRecipes,
            "cracking",
            ItemList.OilCracker,
            true,
            new Dimension(1, 1),
            new Dimension(2, 1),
            new Dimension(1, 1),
            new Dimension(1, 1)),

    PYROLYSE(
            RecipeMaps.pyrolyseRecipes,
            "pyrolyse",
            ItemList.PyrolyseOven,
            true,
            new Dimension(2, 1),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(1, 1)),

    WIREMILL(
            RecipeMaps.wiremillRecipes,
            "wiremill",
            ItemList.Machine_HV_Wiremill,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    BENDER(
            RecipeMaps.benderRecipes,
            "bender",
            ItemList.Machine_HV_Bender,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    ALLOY_SMELTER(
            RecipeMaps.alloySmelterRecipes,
            "alloysmelter",
            ItemList.Machine_HV_AlloySmelter,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    ASSEMBLER(
            RecipeMaps.assemblerRecipes,
            "assembler",
            ItemList.Machine_HV_Assembler,
            true,
            new Dimension(3, 3),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    CIRCUIT_ASSEMBLER(
            RecipeMaps.circuitAssemblerRecipes,
            "circuitassembler",
            ItemList.Machine_HV_CircuitAssembler,
            true,
            new Dimension(3, 2),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    CANNER(
            RecipeMaps.cannerRecipes,
            "canner",
            ItemList.Machine_HV_Canner,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(2, 1),
            new Dimension(0, 0)),

    LATHE(
            RecipeMaps.latheRecipes,
            "lathe",
            ItemList.Machine_HV_Lathe,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(2, 1),
            new Dimension(0, 0)),

    CUTTER(
            RecipeMaps.cutterRecipes,
            "cutter",
            ItemList.Machine_HV_Cutter,
            true,
            new Dimension(2, 1),
            new Dimension(1, 1),
            new Dimension(2, 2),
            new Dimension(0, 0)),

    SLICER(
            RecipeMaps.slicerRecipes,
            "slicer",
            ItemList.Machine_HV_Slicer,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    EXTRUDER(
            RecipeMaps.extruderRecipes,
            "extruder",
            ItemList.Machine_HV_Extruder,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    HAMMER(
            RecipeMaps.hammerRecipes,
            "hammer",
            ItemList.Machine_HV_Hammer,
            true,
            new Dimension(2, 1),
            new Dimension(2, 1),
            new Dimension(2, 1),
            new Dimension(2, 1)),

    AMPLIFIERS(
            RecipeMaps.amplifierRecipes,
            "amplifiers",
            ItemList.Machine_HV_Amplifab,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(0, 0),
            new Dimension(1, 1)),

    MASS_FAB(
            RecipeMaps.massFabFakeRecipes,
            "massfab",
            ItemList.Machine_HV_Massfab,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(1, 1)),

    MULTIBLOCK_ELECTROLYZER(
            RecipeMaps.electrolyzerNonCellRecipes,
            "multiblockelectrolyzer",
            // TODO use GT++ machine icon?
            ItemList.Machine_IV_Electrolyzer,
            true,
            new Dimension(3, 2),
            new Dimension(3, 2),
            new Dimension(3, 2),
            new Dimension(3, 2)),

    MULTIBLOCK_CENTRIFUGE(
            RecipeMaps.centrifugeNonCellRecipes,
            "multiblockcentrifuge",
            // TODO use GT++ machine icon?
            ItemList.Machine_IV_Centrifuge,
            true,
            new Dimension(3, 2),
            new Dimension(3, 2),
            new Dimension(3, 2),
            new Dimension(3, 2)),

    MULTIBLOCK_MIXER(
            RecipeMaps.mixerNonCellRecipes,
            "multiblockmixer",
            // TODO use GT++ machine icon?
            ItemList.Machine_IV_Mixer,
            true,
            new Dimension(3, 3),
            new Dimension(3, 2),
            new Dimension(3, 3),
            new Dimension(3, 2)),

    NANO_FORGE(
            RecipeMaps.nanoForgeRecipes,
            "nanoforge",
            ItemList.NanoForge,
            true,
            new Dimension(3, 2),
            new Dimension(3, 1),
            new Dimension(2, 1),
            new Dimension(0, 0)),

    PCB_FACTORY(
            RecipeMaps.pcbFactoryRecipes,
            "pcbfactory",
            ItemList.PCBFactory,
            true,
            new Dimension(3, 2),
            new Dimension(3, 1),
            new Dimension(3, 3),
            new Dimension(0, 0)),
    ;

    private final gregtech.api.recipe.RecipeMap<?> recipeMap;
    /** Used for IDs. */
    private final String shortName;
    private final String name;
    private final ItemList icon;
    private final boolean shapeless;
    private final Dimension itemInputDimension;
    private final Dimension fluidInputDimension;
    private final Dimension itemOutputDimension;
    private final Dimension fluidOutputDimension;

    RecipeMap(
            gregtech.api.recipe.RecipeMap<?> recipeMap,
            String shortName, ItemList icon, boolean shapeless,
            Dimension itemInputDimension, Dimension fluidInputDimension,
            Dimension itemOutputDimension, Dimension fluidOutputDimension) {
        this.recipeMap = recipeMap;
        this.shortName = shortName;
        this.name = GT_LanguageManager.getTranslation(recipeMap.unlocalizedName);
        this.icon = icon;
        this.shapeless = shapeless;
        this.itemInputDimension = itemInputDimension;
        this.fluidInputDimension = fluidInputDimension;
        this.itemOutputDimension = itemOutputDimension;
        this.fluidOutputDimension = fluidOutputDimension;
    }

    public gregtech.api.recipe.RecipeMap<?> getRecipeMap() {
        return recipeMap;
    }

    public String getShortName() {
        return shortName;
    }

    public String getName() {
        return name;
    }

    public String getName(Voltage voltage) {
        return String.format("%s (%s)", name, voltage.getName());
    }

    public ItemList getIcon() {
        return icon;
    }

    public int getAmperage() {
        return recipeMap.getAmperage();
    }

    public boolean isShapeless() {
        return shapeless;
    }

    public Dimension getItemInputDimension() {
        return itemInputDimension;
    }

    public Dimension getFluidInputDimension() {
        return fluidInputDimension;
    }

    public Dimension getItemOutputDimension() {
        return itemOutputDimension;
    }

    public Dimension getFluidOutputDimension() {
        return fluidOutputDimension;
    }
}
