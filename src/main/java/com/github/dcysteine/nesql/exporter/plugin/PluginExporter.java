package com.github.dcysteine.nesql.exporter.plugin;

public interface PluginExporter {
    /** Performs any needed initialization, such as constructing {@code BaseRecipeType}. */
    default void initialize() {}

    /** Processes the plugin's recipes, and anything else that should be exported. */
    default void process() {}

    /**
     * Performs any processing that requires all items, fluids, and recipes to have already been
     * persisted.
     */
    default void postProcess() {}
}
