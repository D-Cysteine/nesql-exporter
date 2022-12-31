package com.github.dcysteine.nesql.exporter.plugin;

/** Interface for plugins. */
public interface Plugin {
    // TODO add auto-logging here
    /** Process the plugin's recipes, and anything else that should be exported. */
    default void process() {}

    /**
     * Perform any processing that requires all items, fluids, and recipes to have already been
     * persisted.
     */
    default void postProcess() {}
}
