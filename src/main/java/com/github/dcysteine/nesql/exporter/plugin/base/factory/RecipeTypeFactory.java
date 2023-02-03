package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.recipe.Dimension;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;

import java.util.ArrayList;
import java.util.List;

public class RecipeTypeFactory extends EntityFactory<RecipeType, String> {
    public RecipeTypeFactory(PluginExporter exporter) {
        super(exporter);
    }

    /** {@code idParts} will be joined with {@link IdUtil#ID_SEPARATOR} to form the ID. */
    public RecipeType get(
            String[] idParts, String category, String type, Item icon, String iconInfo,
            boolean shapeless,
            Dimension itemInputDimension, Dimension fluidInputDimension,
            Dimension itemOutputDimension, Dimension fluidOutputDimension) {
        String id = IdPrefixUtil.RECIPE_TYPE.applyPrefix(idParts);
        RecipeType recipeType =
                new RecipeType(
                        id, category, type, icon, iconInfo, shapeless,
                        itemInputDimension, fluidInputDimension,
                        itemOutputDimension, fluidOutputDimension);
        return findOrPersist(RecipeType.class, recipeType);
    }

    public Builder newBuilder() {
        return new Builder();
    }

    public class Builder {
        private String[] idParts = null;
        private String category = null;
        private String type = null;
        private Item icon = null;
        private String iconInfo = "";
        private Boolean shapeless = false;
        private Dimension itemInputDimension = new Dimension(0, 0);
        private Dimension fluidInputDimension = new Dimension(0, 0);
        private Dimension itemOutputDimension = new Dimension(0, 0);
        private Dimension fluidOutputDimension = new Dimension(0, 0);

        private Builder() {}

        public Builder setId(String... idParts) {
            this.idParts = idParts;
            return this;
        }

        public Builder setCategory(String category) {
            this.category = category;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setIcon(Item icon) {
            this.icon = icon;
            return this;
        }

        public Builder setIconInfo(String iconInfo) {
            this.iconInfo = iconInfo;
            return this;
        }

        public Builder setShapeless(boolean shapeless) {
            this.shapeless = shapeless;
            return this;
        }

        public Builder setItemInputDimension(int width, int height) {
            this.itemInputDimension = new Dimension(width, height);
            return this;
        }

        public Builder setItemInputDimension(Dimension dimension) {
            this.itemInputDimension = dimension;
            return this;
        }

        public Builder setFluidInputDimension(int width, int height) {
            this.fluidInputDimension = new Dimension(width, height);
            return this;
        }

        public Builder setFluidInputDimension(Dimension dimension) {
            this.fluidInputDimension = dimension;
            return this;
        }

        public Builder setItemOutputDimension(int width, int height) {
            this.itemOutputDimension = new Dimension(width, height);
            return this;
        }

        public Builder setItemOutputDimension(Dimension dimension) {
            this.itemOutputDimension = dimension;
            return this;
        }

        public Builder setFluidOutputDimension(int width, int height) {
            this.fluidInputDimension = new Dimension(width, height);
            return this;
        }

        public Builder setFluidOutputDimension(Dimension dimension) {
            this.fluidOutputDimension = dimension;
            return this;
        }

        public RecipeType build() {
            return get(
                    idParts, category, type, icon, iconInfo, shapeless,
                    itemInputDimension, fluidInputDimension,
                    itemOutputDimension, fluidOutputDimension);
        }

        private void verifyAllFieldsSet() {
            List<String> missingFields = new ArrayList<>();

            if (idParts == null) {
                missingFields.add("idParts");
            }
            if (category == null) {
                missingFields.add("category");
            }
            if (type == null) {
                missingFields.add("type");
            }
            if (icon == null) {
                missingFields.add("icon");
            }
            if (iconInfo == null) {
                missingFields.add("iconInfo");
            }
            if (shapeless == null) {
                missingFields.add("shapeless");
            }
            if (itemInputDimension == null) {
                missingFields.add("itemInputDimension");
            }
            if (fluidInputDimension == null) {
                missingFields.add("fluidInputDimension");
            }
            if (itemOutputDimension == null) {
                missingFields.add("itemOutputDimension");
            }
            if (fluidOutputDimension == null) {
                missingFields.add("fluidOutputDimension");
            }

            if (!missingFields.isEmpty()) {
                throw new IllegalStateException(
                        "Missing fields in RecipeTypeFactory.Builder:\n  " + missingFields);
            }
        }
    }
}
