package com.github.dcysteine.nesql.exporter.plugin.thaumcraft;

import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.thaumcraft.postprocessor.AspectPostProcessor;
import com.github.dcysteine.nesql.exporter.plugin.thaumcraft.processor.AspectProcessor;
import jakarta.persistence.EntityManager;

/** Plugin which exports BetterQuesting quests. */
public class ThaumcraftPluginExporter implements PluginExporter {
    private final EntityManager entityManager;

    public ThaumcraftPluginExporter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void process() {
        new AspectProcessor(entityManager).process();
    }

    @Override
    public void postProcess() {
        new AspectPostProcessor(entityManager).postProcess();
    }
}
