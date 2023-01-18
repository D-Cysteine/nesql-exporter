package com.github.dcysteine.nesql.exporter.util;

import com.google.common.base.Joiner;

/** Helper class for applying a unique prefix to all persisted entity IDs. */
public enum IdPrefixUtil {
    ITEM("i"),
    FLUID("f"),
    ITEM_GROUP("ig"),
    FLUID_GROUP("fg"),
    RECIPE("r"),
    RECIPE_TYPE("rt"),
    ;

    private final String prefix;

    private IdPrefixUtil(String... prefixParts) {
        prefix = Joiner.on(IdUtil.ID_SEPARATOR).join(prefixParts) + IdUtil.ID_SEPARATOR;
    }

    public String getPrefix() {
        return prefix;
    }

    public String applyPrefix(String id) {
        return prefix + id;
    }
}
