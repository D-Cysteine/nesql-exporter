package com.github.dcysteine.nesql.exporter.plugin.gregtech.util;

import com.github.dcysteine.nesql.sql.base.recipe.Dimension;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Recipe;

/** Enum of supported GregTech recipe maps. */
public enum RecipeMap {
    ORE_WASHER(
            GT_Recipe.GT_Recipe_Map.sOreWasherRecipes,
            "orewasher",
            ItemList.Machine_HV_OreWasher,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(3, 1),
            new Dimension(0, 0)),

    THERMAL_CENTRIFUGE(
            GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes,
            "thermalcentrifuge",
            ItemList.Machine_HV_ThermalCentrifuge,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(3, 1),
            new Dimension(0, 0)),

    COMPRESSOR(
            GT_Recipe.GT_Recipe_Map.sCompressorRecipes,
            "compressor",
            ItemList.Machine_HV_Compressor,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    EXTRACTOR(
            GT_Recipe.GT_Recipe_Map.sExtractorRecipes,
            "extractor",
            ItemList.Machine_HV_Extractor,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    DISASSEMBLER(
            GT_Recipe.GT_Recipe_Map.sDisassemblerRecipes,
            "disassembler",
            ItemList.Machine_HV_Disassembler,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(3, 3),
            new Dimension(0, 0)),

    SCANNER(
            GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes,
            "scanner",
            ItemList.Machine_HV_Scanner,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    ROCK_BREAKER(
            GT_Recipe.GT_Recipe_Map.sRockBreakerFakeRecipes,
            "rockbreaker",
            ItemList.Machine_HV_RockBreaker,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    REPLICATOR(
            GT_Recipe.GT_Recipe_Map.sReplicatorFakeRecipes,
            "replicator",
            ItemList.Machine_HV_Replicator,
            true,
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    ASSEMBLY_LINE(
            GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes,
            "assemblyline",
            ItemList.Machine_Multi_Assemblyline,
            false,
            new Dimension(4, 4),
            new Dimension(4, 1),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    PLASMA_ARC_FURNACE(
            GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes,
            "plasmaarcfurnace",
            ItemList.Machine_HV_PlasmaArcFurnace,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(2, 2),
            new Dimension(1, 1)),

    ARC_FURNACE(
            GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes,
            "arcfurnace",
            ItemList.Machine_HV_ArcFurnace,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(2, 2),
            new Dimension(0, 0)),

    PRINTER(
            GT_Recipe.GT_Recipe_Map.sPrinterRecipes,
            "printer",
            ItemList.Machine_HV_Printer,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    SIFTER(
            GT_Recipe.GT_Recipe_Map.sSifterRecipes,
            "sifter",
            ItemList.Machine_HV_Sifter,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(3, 3),
            new Dimension(1, 1)),

    PRESS(
            GT_Recipe.GT_Recipe_Map.sPressRecipes,
            "press",
            ItemList.Machine_HV_Press,
            true,
            new Dimension(3, 2),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    LASER_ENGRAVER(
            GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes,
            "laserengraver",
            ItemList.Machine_HV_LaserEngraver,
            true,
            new Dimension(2, 2),
            new Dimension(2, 1),
            new Dimension(2, 2),
            new Dimension(2, 1)),

    MIXER(
            GT_Recipe.GT_Recipe_Map.sMixerRecipes,
            "mixer",
            ItemList.Machine_HV_Mixer,
            true,
            new Dimension(3, 3),
            new Dimension(1, 1),
            new Dimension(2, 2),
            new Dimension(1, 1)),

    AUTOCLAVE(
            GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes,
            "autoclave",
            ItemList.Machine_HV_Autoclave,
            true,
            new Dimension(2, 1),
            new Dimension(1, 1),
            new Dimension(2, 2),
            new Dimension(1, 1)),

    ELECTROMAGNETIC_SEPARATOR(
            GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes,
            "electromagneticseparator",
            ItemList.Machine_HV_ElectromagneticSeparator,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(3, 1),
            new Dimension(0, 0)),

    POLARIZER(
            GT_Recipe.GT_Recipe_Map.sPolarizerRecipes,
            "polarizer",
            ItemList.Machine_HV_Polarizer,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    MACERATOR(
            GT_Recipe.GT_Recipe_Map.sMaceratorRecipes,
            "macerator",
            ItemList.Machine_HV_Macerator,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(2, 2),
            new Dimension(0, 0)),

    CHEMICAL_BATH(
            GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes,
            "chemicalbath",
            ItemList.Machine_HV_ChemicalBath,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(3, 1),
            new Dimension(1, 1)),

    FLUID_CANNER(
            GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes,
            "fluidcanner",
            ItemList.Machine_HV_FluidCanner,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(1, 1)),

    BREWING(
            GT_Recipe.GT_Recipe_Map.sBrewingRecipes,
            "brewing",
            ItemList.Machine_HV_Brewery,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(1, 1)),

    FLUID_HEATER(
            GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes,
            "fluidheater",
            ItemList.Machine_HV_FluidHeater,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(1, 1)),

    DISTILLERY(
            GT_Recipe.GT_Recipe_Map.sDistilleryRecipes,
            "distillery",
            ItemList.Machine_HV_Distillery,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(1, 1)),

    FERMENTING(
            GT_Recipe.GT_Recipe_Map.sFermentingRecipes,
            "fermenting",
            ItemList.Machine_HV_Fermenter,
            true,
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(1, 1)),

    FLUID_SOLIDIFICATION(
            GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes,
            "fluidsolidification",
            ItemList.Machine_HV_FluidSolidifier,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    FLUID_EXTRACTION(
            GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes,
            "fluidextraction",
            ItemList.Machine_HV_FluidExtractor,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(1, 1)),

    BOXINATOR(
            GT_Recipe.GT_Recipe_Map.sBoxinatorRecipes,
            "boxinator",
            ItemList.Machine_HV_Boxinator,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    UNBOXINATOR(
            GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes,
            "unboxinator",
            ItemList.Machine_HV_Unboxinator,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(2, 1),
            new Dimension(0, 0)),

    FUSION(
            GT_Recipe.GT_Recipe_Map.sFusionRecipes,
            "fusion",
            ItemList.FusionComputer_LuV,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    CENTRIFUGE(
            GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes,
            "centrifuge",
            ItemList.Machine_HV_Centrifuge,
            true,
            new Dimension(2, 1),
            new Dimension(1, 1),
            new Dimension(3, 2),
            new Dimension(1, 1)),

    ELECTROLYZER(
            GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes,
            "electrolyzer",
            ItemList.Machine_HV_Electrolyzer,
            true,
            new Dimension(2, 1),
            new Dimension(1, 1),
            new Dimension(3, 2),
            new Dimension(1, 1)),

    BLAST(
            GT_Recipe.GT_Recipe_Map.sBlastRecipes,
            "blast",
            ItemList.Machine_Multi_BlastFurnace,
            true,
            new Dimension(3, 2),
            new Dimension(1, 1),
            new Dimension(3, 2),
            new Dimension(1, 1)),

    PRIMITIVE_BLAST(
            GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes,
            "primitiveblast",
            ItemList.Machine_Bricked_BlastFurnace,
            true,
            new Dimension(3, 1),
            new Dimension(0, 0),
            new Dimension(3, 1),
            new Dimension(0, 0)),

    IMPLOSION(
            GT_Recipe.GT_Recipe_Map.sImplosionRecipes,
            "implosion",
            ItemList.Machine_Multi_ImplosionCompressor,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(2, 1),
            new Dimension(0, 0)),

    VACUUM(
            GT_Recipe.GT_Recipe_Map.sVacuumRecipes,
            "vacuum",
            ItemList.Machine_Multi_VacuumFreezer,
            true,
            new Dimension(1, 1),
            new Dimension(2, 1),
            new Dimension(1, 1),
            new Dimension(1, 1)),

    CHEMICAL(
            GT_Recipe.GT_Recipe_Map.sChemicalRecipes,
            "chemical",
            ItemList.Machine_HV_ChemicalReactor,
            true,
            new Dimension(2, 1),
            new Dimension(1, 1),
            new Dimension(2, 1),
            new Dimension(1, 1)),

    MULTIBLOCK_CHEMICAL(
            GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes,
            "multiblockchemical",
            ItemList.Machine_Multi_LargeChemicalReactor,
            true,
            new Dimension(3, 2),
            new Dimension(3, 2),
            new Dimension(3, 2),
            new Dimension(3, 2)),

    DISTILLATION(
            GT_Recipe.GT_Recipe_Map.sDistillationRecipes,
            "distillation",
            ItemList.Distillation_Tower,
            true,
            new Dimension(2, 1),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(3, 4)),

    CRACKING(
            GT_Recipe.GT_Recipe_Map.sCrackingRecipes,
            "cracking",
            ItemList.OilCracker,
            true,
            new Dimension(1, 1),
            new Dimension(2, 1),
            new Dimension(1, 1),
            new Dimension(1, 1)),

    PYROLYSE(
            GT_Recipe.GT_Recipe_Map.sPyrolyseRecipes,
            "pyrolyse",
            ItemList.PyrolyseOven,
            true,
            new Dimension(2, 1),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(1, 1)),

    WIREMILL(
            GT_Recipe.GT_Recipe_Map.sWiremillRecipes,
            "wiremill",
            ItemList.Machine_HV_Wiremill,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    BENDER(
            GT_Recipe.GT_Recipe_Map.sBenderRecipes,
            "bender",
            ItemList.Machine_HV_Bender,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    ALLOY_SMELTER(
            GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes,
            "alloysmelter",
            ItemList.Machine_HV_AlloySmelter,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    ASSEMBLER(
            GT_Recipe.GT_Recipe_Map.sAssemblerRecipes,
            "assembler",
            ItemList.Machine_HV_Assembler,
            true,
            new Dimension(3, 3),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    CIRCUIT_ASSEMBLER(
            GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
            "circuitassembler",
            ItemList.Machine_HV_CircuitAssembler,
            true,
            new Dimension(3, 2),
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    CANNER(
            GT_Recipe.GT_Recipe_Map.sCannerRecipes,
            "canner",
            ItemList.Machine_HV_Canner,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(2, 1),
            new Dimension(0, 0)),

    LATHE(
            GT_Recipe.GT_Recipe_Map.sLatheRecipes,
            "lathe",
            ItemList.Machine_HV_Lathe,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(2, 1),
            new Dimension(0, 0)),

    CUTTER(
            GT_Recipe.GT_Recipe_Map.sCutterRecipes,
            "cutter",
            ItemList.Machine_HV_Cutter,
            true,
            new Dimension(2, 1),
            new Dimension(1, 1),
            new Dimension(2, 2),
            new Dimension(0, 0)),

    SLICER(
            GT_Recipe.GT_Recipe_Map.sSlicerRecipes,
            "slicer",
            ItemList.Machine_HV_Slicer,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    EXTRUDER(
            GT_Recipe.GT_Recipe_Map.sExtruderRecipes,
            "extruder",
            ItemList.Machine_HV_Extruder,
            true,
            new Dimension(2, 1),
            new Dimension(0, 0),
            new Dimension(1, 1),
            new Dimension(0, 0)),

    HAMMER(
            GT_Recipe.GT_Recipe_Map.sHammerRecipes,
            "hammer",
            ItemList.Machine_HV_Hammer,
            true,
            new Dimension(2, 1),
            new Dimension(2, 1),
            new Dimension(2, 1),
            new Dimension(2, 1)),

    AMPLIFIERS(
            GT_Recipe.GT_Recipe_Map.sAmplifiers,
            "amplifiers",
            ItemList.Machine_HV_Amplifab,
            true,
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(0, 0),
            new Dimension(1, 1)),

    MASS_FAB(
            GT_Recipe.GT_Recipe_Map.sMassFabFakeRecipes,
            "massfab",
            ItemList.Machine_HV_Massfab,
            true,
            new Dimension(1, 1),
            new Dimension(1, 1),
            new Dimension(0, 0),
            new Dimension(1, 1)),
    ;

    private final GT_Recipe.GT_Recipe_Map recipeMap;
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
            GT_Recipe.GT_Recipe_Map recipeMap, String shortName, ItemList icon, boolean shapeless,
            Dimension itemInputDimension, Dimension fluidInputDimension,
            Dimension itemOutputDimension, Dimension fluidOutputDimension) {
        this.recipeMap = recipeMap;
        this.shortName = shortName;
        this.name = GT_LanguageManager.getTranslation(recipeMap.mUnlocalizedName);
        this.icon = icon;
        this.shapeless = shapeless;
        this.itemInputDimension = itemInputDimension;
        this.fluidInputDimension = fluidInputDimension;
        this.itemOutputDimension = itemOutputDimension;
        this.fluidOutputDimension = fluidOutputDimension;
    }

    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
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
        return recipeMap.mAmperage;
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
