package com.github.dcysteine.nesql.exporter.registry;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public enum ModDependency {
    // GregTech 5 shares a mod ID with GregTech 6, so we must also check the mod version.
    GREGTECH_5("gregtech") {
        @Override
        public boolean isLoaded() {
            if (super.isLoaded()) {
                return !getVersion().startsWith("GT6");
            } else {
                return false;
            }
        }
    },

    AVARITIA("Avaritia"),
    FORESTRY("Forestry"),
    THAUMCRAFT("Thaumcraft"),
    THAUMCRAFT_NEI("thaumcraftneiplugin"),
    BETTER_QUESTING("betterquesting"),
    ;

    public final String modId;

    ModDependency(String modId) {
        this.modId = modId;
    }

    public boolean isLoaded() {
        return Loader.isModLoaded(modId);
    }

    public String getVersion() {
        return getMod().getVersion();
    }

    public ModContainer getMod() {
        return Loader.instance().getIndexedModList().get(modId);
    }
}
