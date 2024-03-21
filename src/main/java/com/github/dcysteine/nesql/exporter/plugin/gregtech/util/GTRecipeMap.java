package com.github.dcysteine.nesql.exporter.plugin.gregtech.util;

import com.github.dcysteine.nesql.sql.base.recipe.Dimension;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static codechicken.nei.recipe.RecipeCatalysts.getRecipeCatalysts;

/** Enum of supported GregTech recipe maps. */
public class GTRecipeMap {
    public static HashMap<String, GTRecipeMap> allNEIRecipeMaps=new HashMap<>();
    private final gregtech.api.recipe.RecipeMap<? extends RecipeMapBackend> recipeMap;
    /** Used for IDs. */
    private final String shortName;
    private final String name;
    private final ItemStack icon;
    private final boolean shapeless;
    private final Dimension itemInputDimension;
    private final Dimension fluidInputDimension;
    private final Dimension itemOutputDimension;
    private final Dimension fluidOutputDimension;
    GTRecipeMap(
            gregtech.api.recipe.RecipeMap<? extends RecipeMapBackend> recipeMap, String shortName, ItemStack icon, boolean shapeless,
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
    public static void makeGTRecipe(){
        for(gregtech.api.recipe.RecipeMap<?> oriGTRecipeMap:gregtech.api.recipe.RecipeMap.ALL_RECIPE_MAPS.values()){
            List<ItemStack> catalysts=getRecipeCatalysts(oriGTRecipeMap.unlocalizedName).stream().map(var->var.item).collect(Collectors.toList());
            if(catalysts.isEmpty())continue;
            GTRecipeMap gtRecipeMap=new GTRecipeMap(
                    oriGTRecipeMap,
                    oriGTRecipeMap.unlocalizedName,
                    catalysts.get(catalysts.size()-1),
                    true,
                    getDimension(oriGTRecipeMap.getFrontend().getUIProperties().itemInputPositionsGetter.apply(
                            oriGTRecipeMap.getFrontend().getUIProperties().maxItemInputs)),
                    getDimension(oriGTRecipeMap.getFrontend().getUIProperties().fluidInputPositionsGetter.apply(
                            oriGTRecipeMap.getFrontend().getUIProperties().maxFluidInputs)),
                    getDimension(oriGTRecipeMap.getFrontend().getUIProperties().itemOutputPositionsGetter.apply(
                            oriGTRecipeMap.getFrontend().getUIProperties().maxItemOutputs)),
                    getDimension(oriGTRecipeMap.getFrontend().getUIProperties().fluidOutputPositionsGetter.apply(
                            oriGTRecipeMap.getFrontend().getUIProperties().maxFluidOutputs))
            );
            allNEIRecipeMaps.put(oriGTRecipeMap.unlocalizedName,gtRecipeMap);
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("RecipeMap "+
                    StatCollector.translateToLocal(oriGTRecipeMap.unlocalizedName)+" export!"));
        }
    }
    public static Dimension getDimension(List<Pos2d> posList){
        int[] dimension=new int[]{0,0};
        List<Integer> x=new ArrayList<>(),y=new ArrayList<>();
        for(Pos2d pos:posList){
            if(!x.contains(pos.x)){
                dimension[0]++;
                x.add(pos.x);
            }
            if(!y.contains(pos.y)){
                dimension[1]++;
                y.add(pos.y);
            }
        }
        return new Dimension(dimension[0],dimension[1]);
    }
    public gregtech.api.recipe.RecipeMap<? extends RecipeMapBackend> getRecipeMap() {
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

    public ItemStack getIcon() {
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
