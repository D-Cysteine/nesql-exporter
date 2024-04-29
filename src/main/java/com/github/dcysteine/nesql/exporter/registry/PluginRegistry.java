package com.github.dcysteine.nesql.exporter.registry;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.BasePluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.forge.ForgePluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.gregtech.GregTechPluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.minecraft.MinecraftPluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.mobsinfo.MobsInfoPluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.nei.NeiPluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.quest.QuestPluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.thaumcraft.ThaumcraftPluginExporter;
import com.github.dcysteine.nesql.sql.Plugin;
import com.google.common.collect.ImmutableList;

import java.util.EnumMap;
import java.util.Map;


/** Registry of plugins. Register new plugins here! */
public class PluginRegistry {
    private static final ImmutableList<RegistryEntry> entries;

    static {
        ImmutableList.Builder<RegistryEntry> builder = ImmutableList.builder();

        // Add new plugins here!
        builder.add(RegistryEntry.create(Plugin.BASE, BasePluginExporter::new));
        builder.add(RegistryEntry.create(Plugin.MINECRAFT, MinecraftPluginExporter::new));
        builder.add(RegistryEntry.create(Plugin.NEI, NeiPluginExporter::new));
        builder.add(RegistryEntry.create(Plugin.FORGE, ForgePluginExporter::new));

        builder.add(
                RegistryEntry.create(
                        Plugin.MOBS_INFO, MobsInfoPluginExporter::new, ModDependency.MOBS_INFO));

        builder.add(
                RegistryEntry.create(
                        Plugin.GREGTECH, GregTechPluginExporter::new, ModDependency.GREGTECH_5));
        builder.add(
                RegistryEntry.create(
                        Plugin.THAUMCRAFT, ThaumcraftPluginExporter::new,
                        ModDependency.THAUMCRAFT, ModDependency.THAUMCRAFT_NEI));
        builder.add(
                RegistryEntry.create(
                        Plugin.QUEST, QuestPluginExporter::new, ModDependency.BETTER_QUESTING));

        entries = builder.build();
    }

    private final Map<Plugin, PluginExporter> activePlugins = new EnumMap<>(Plugin.class);

    /** Constructs plugins whose dependencies are met. Returns a list of activated plugins. */
    public Map<Plugin, PluginExporter> initialize(ExporterState exporterState) {
        entries.stream()
                .filter(RegistryEntry::areDependenciesSatisfied)
                .filter(RegistryEntry::isEnabled)
                .forEach(
                        entry ->
                                activePlugins.put(
                                        entry.getPlugin(), entry.instantiate(exporterState)));

        if (!activePlugins.containsKey(Plugin.BASE)) {
            throw new IllegalStateException("base plugin must be enabled!");
        }
        return activePlugins;
    }

    public Map<Plugin, PluginExporter> getActivePlugins() {
        return activePlugins;
    }

    public void initializePlugins() {
        activePlugins.values().forEach(PluginExporter::initialize);
    }

    public void processPlugins() {
        activePlugins.values().forEach(PluginExporter::process);
    }

    public void postProcessPlugins() {
        activePlugins.values().forEach(PluginExporter::postProcess);
    }
}
