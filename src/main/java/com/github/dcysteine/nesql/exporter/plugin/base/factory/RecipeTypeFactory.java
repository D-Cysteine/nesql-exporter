package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.recipe.Dimension;
import com.github.dcysteine.nesql.sql.base.recipe.RecipeType;
import com.google.common.base.Joiner;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class RecipeTypeFactory extends EntityFactory<RecipeType, String> {
    public RecipeTypeFactory(EntityManager entityManager) {
        super(entityManager);
    }

    /** {@code idParts} will be joined with {@link IdUtil#ID_SEPARATOR} to form the ID. */
    public RecipeType getRecipeType(
            String[] idParts, String category, String type, Item icon, boolean shapeless,
            Dimension itemInputDimension, Dimension fluidInputDimension,
            Dimension itemOutputDimension, Dimension fluidOutputDimension) {
        String id =
                IdPrefixUtil.RECIPE_TYPE.applyPrefix(Joiner.on(IdUtil.ID_SEPARATOR).join(idParts));
        RecipeType recipeType =
                new RecipeType(
                        id, category, type, icon, shapeless,
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

        public Builder setShapeless(boolean shapeless) {
            this.shapeless = shapeless;
            return this;
        }

        public Builder setItemInputDimension(int width, int height) {
            this.itemInputDimension = new Dimension(width, height);
            return this;
        }

        public Builder setFluidInputDimension(int width, int height) {
            this.fluidInputDimension = new Dimension(width, height);
            return this;
        }

        public Builder setItemOutputDimension(int width, int height) {
            this.itemOutputDimension = new Dimension(width, height);
            return this;
        }

        public Builder setFluidOutputDimension(int width, int height) {
            this.fluidInputDimension = new Dimension(width, height);
            return this;
        }

        public RecipeType build() {
            return getRecipeType(
                    idParts, category, type, icon, shapeless,
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
