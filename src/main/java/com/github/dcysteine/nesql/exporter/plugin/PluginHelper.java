package com.github.dcysteine.nesql.exporter.plugin;

import com.github.dcysteine.nesql.sql.Plugin;
import jakarta.persistence.EntityManager;

/** Abstract class which helps with the boilerplate around accessing shared state. */
public abstract class PluginHelper {
    protected final PluginExporter exporter;
    protected final Plugin plugin;
    protected final org.apache.logging.log4j.Logger logger;
    protected final ExporterState exporterState;
    protected final EntityManager entityManager;

    public PluginHelper(PluginExporter exporter) {
        this.exporter = exporter;
        this.plugin = exporter.getPlugin();
        this.logger = exporter.getLogger();
        this.exporterState = exporter.getDatabase();
        this.entityManager = exporterState.getEntityManager();
    }
}
