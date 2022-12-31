package com.github.dcysteine.nesql.exporter.plugin;

/** Interface for plugins. */
public abstract class Plugin {
    // TODO add auto-logging here
    /** Process the plugin's recipes, and anything else that should be exported. */
    public void process() {}

    /**
     * Perform any processing that requires all items, fluids, and recipes to have already been
     * persisted.
     */
    public void postProcess() {}
}
