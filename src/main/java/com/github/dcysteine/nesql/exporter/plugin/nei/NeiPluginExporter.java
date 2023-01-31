package com.github.dcysteine.nesql.exporter.plugin.nei;

import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.nei.processor.NeiItemListProcessor;
import jakarta.persistence.EntityManager;

/** Plugin which exports the NEI item list. */
public class NeiPluginExporter implements PluginExporter {
    private final EntityManager entityManager;

    public NeiPluginExporter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void process() {
        new NeiItemListProcessor(entityManager).process();
    }
}
