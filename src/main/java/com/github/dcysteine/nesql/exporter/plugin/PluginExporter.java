package com.github.dcysteine.nesql.exporter.plugin;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.sql.Plugin;

/** Abstract class which helps with setting up and passing around state variables. */
public abstract class PluginExporter {
    protected final Plugin plugin;
    protected final org.apache.logging.log4j.Logger logger;
    protected final ExporterState exporterState;

    public PluginExporter(Plugin plugin, ExporterState exporterState) {
        this.plugin = plugin;
        this.logger = Logger.getLogger(plugin);
        this.exporterState = exporterState;
    }

    public final Plugin getPlugin() {
        return plugin;
    }

    public final org.apache.logging.log4j.Logger getLogger() {
        return logger;
    }

    public final ExporterState getDatabase() {
        return exporterState;
    }

    /**
     * Performs any needed initialization,
     * such as constructing recipe types or registering listeners.
     */
    public void initialize() {}

    /** Processes the plugin's recipes, and anything else that should be exported. */
    public void process() {}

    /**
     * Performs any processing that requires all items, fluids, and recipes to have already been
     * persisted.
     */
    public void postProcess() {}
}
