package com.github.dcysteine.nesql.sql.base.recipe;

import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;

/** Contains information the recipe's type, icon, and links to plugin tables with more info. */
@Embeddable
@EqualsAndHashCode
@ToString
public class RecipeInfo implements Comparable<RecipeInfo> {
    private String category;
    private String type;

    @ManyToOne
    private Item icon;

    private boolean shapeless;

    /** Needed by Hibernate. */
    protected RecipeInfo() {}

    public RecipeInfo(String category, String type, Item icon, boolean shapeless) {
        this.category = category;
        this.type = type;
        this.icon = icon;
        this.shapeless = shapeless;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public Item getIcon() {
        return icon;
    }

    public boolean isShapeless() {
        return shapeless;
    }

    @Override
    public int compareTo(RecipeInfo other) {
        return Comparator.comparing(RecipeInfo::getCategory)
                .thenComparing(RecipeInfo::getType)
                .thenComparing(RecipeInfo::isShapeless)
                .compare(this, other);
    }
}
