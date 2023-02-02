package com.github.dcysteine.nesql.exporter.plugin;

import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.EntityManager;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Class that holds the accessor for the database, stores any needed state, and handles listeners.
 */
public class ExporterState {
    public interface ItemListener extends BiConsumer<Item, ItemStack> {}

    private final EntityManager entityManager;
    private int itemCount;
    private int fluidCount;

    // TODO do we want to provide some sort of RecipeType registry here?
    // If so, something like: Map<Class<T>, T> where T is a plugin-custom recipe repository class.

    private final List<ItemListener> itemListeners;

    // TODO do we want to add listeners on iteration through the vanilla crafting recipe list?

    public ExporterState(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.itemCount = 0;
        this.fluidCount = 0;
        this.itemListeners = new ArrayList<>();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public int incrementItemCount() {
        return ++itemCount;
    }

    public int incrementFluidCount() {
        return ++fluidCount;
    }

    public void addItemListener(ItemListener listener) {
        itemListeners.add(listener);
    }

    public void invokeItemListeners(Item item, ItemStack itemStack) {
        itemListeners.forEach(listener -> listener.accept(item, itemStack));
    }
}
