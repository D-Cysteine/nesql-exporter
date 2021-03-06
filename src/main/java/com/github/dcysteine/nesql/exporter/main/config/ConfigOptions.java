package com.github.dcysteine.nesql.exporter.main.config;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.common.config.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public final class ConfigOptions {
    private static final List<Option<?>> allOptions = new ArrayList<>();

    public static final Option<Boolean> AUTO_EXPORT_ON_CONNECT =
            new BooleanOption(
                    Category.OPTIONS, "auto_export_on_connect", false,
                    "Whether to automatically export upon connecting to a world."
                            + "\nThe default filename will be used.")
                    .register();

    public static final Option<Boolean> ENABLE_CONFIG_FILE =
            new BooleanOption(
                    Category.OPTIONS, "enable_config_file", false,
                    "Whether to generate a config file."
                            + "\nConfig changes will be forgotten if this option is not enabled!"
                            + "\nDISABLING THIS OPTION WILL DELETE YOUR CONFIG FILE!")
                    .register();

    public static final Option<Boolean> EXPORT_ICONS_TO_FILES =
            new BooleanOption(
                    Category.OPTIONS, "export_icons_to_files", false,
                    "Whether to save rendered icons into individual files when exporting."
                            + "\nHas no effect if render_icons is false.")
                    .register();

    public static final Option<Integer> ICON_DIMENSION =
            new IntegerOption(
                    Category.OPTIONS, "icon_dimension", 64,
                    "The size of rendered icons, in pixels. Should probably be a multiple of 32."
                            + "\nHas no effect if render_icons is false.")
                    .register();

    public static final Option<Boolean> RENDER_ICONS =
            new BooleanOption(
                    Category.OPTIONS, "render_icons", true,
                    "Whether to render item and fluid icons when exporting.")
                    .register();

    public static final Option<Integer> RENDER_ICONS_PER_TICK =
            new IntegerOption(
                    Category.OPTIONS, "render_icons_per_tick", 256,
                    "The number of icons to render per tick. Lower this if your computer"
                            + " can't handle the default.")
                    .register();

    public enum Category {
        OPTIONS("options");

        private final String name;

        Category(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public abstract static class Option<T> implements Supplier<T> {
        final Category category;
        final String key;
        final T defaultValue;
        final String comment;
        final boolean requiresRestart;

        Property property;

        Option(
                Category category, String key, T defaultValue, String comment,
                boolean requiresRestart) {
            this.category = category;
            this.key = key;
            this.defaultValue = defaultValue;
            this.comment = comment + buildDefaultComment(defaultValue);
            this.requiresRestart = requiresRestart;
        }

        Option(Category category, String key, T defaultValue, String comment) {
            this(category, key, defaultValue, comment, false);
        }

        /** Chain this method right after construction. */
        Option<T> register() {
            allOptions.add(this);
            return this;
        }

        public void initialize() {
            property = getProperty();
            property.setRequiresMcRestart(requiresRestart);

            // Load this option, so that it gets saved if it's missing from the config.
            get();
        }

        /**
         * Sadly, this abstract method is needed because we cannot in-line getting the property in
         * {@link #initialize()} due to type shenanigans.
         */
        abstract Property getProperty();

        @Override
        public abstract T get();
    }

    public static final class BooleanOption extends Option<Boolean> {
        private BooleanOption(Category category, String key, boolean defaultValue, String comment) {
            super(category, key, defaultValue, comment);
        }

        private BooleanOption(
                Category category, String key, boolean defaultValue, String comment,
                boolean requiresRestart) {
            super(category, key, defaultValue, comment, requiresRestart);
        }

        @Override
        Property getProperty() {
            return Config.CONFIG.get(category.toString(), key, defaultValue, comment);
        }

        @Override
        public Boolean get() {
            return property.getBoolean();
        }
    }

    public static final class IntegerOption extends Option<Integer> {
        private IntegerOption(Category category, String key, int defaultValue, String comment) {
            super(category, key, defaultValue, comment);
        }

        private IntegerOption(
                Category category, String key, int defaultValue, String comment,
                boolean requiresRestart) {
            super(category, key, defaultValue, comment, requiresRestart);
        }

        @Override
        Property getProperty() {
            return Config.CONFIG.get(category.toString(), key, defaultValue, comment);
        }

        @Override
        public Integer get() {
            return property.getInt();
        }
    }

    public static final class StringArrayOption extends Option<String[]> {
        private StringArrayOption(
                Category category, String key, String[] defaultValue, String comment) {
            super(category, key, defaultValue, comment);
        }

        private StringArrayOption(
                Category category, String key, String[] defaultValue, String comment,
                boolean requiresRestart) {
            super(category, key, defaultValue, comment, requiresRestart);
        }

        @Override
        Property getProperty() {
            return Config.CONFIG.get(category.toString(), key, defaultValue, comment);
        }

        @Override
        public String[] get() {
            return property.getStringList();
        }
    }

    // Static class.
    private ConfigOptions() {}

    static void setCategoryComments() {
        Config.CONFIG.setCategoryComment(Category.OPTIONS.toString(), "General usage options.");
    }

    static ImmutableList<Option<?>> getAllOptions() {
        return ImmutableList.copyOf(allOptions);
    }

    private static String buildDefaultComment(Object defaultValue) {
        String toString;
        if (defaultValue instanceof String[]) {
            // String[] does not have a nice default toString() implementation.
            toString = Arrays.toString((String[]) defaultValue);
        } else {
            toString = defaultValue.toString();
        }

        return String.format("\nDefault: %s", toString);
    }
}
