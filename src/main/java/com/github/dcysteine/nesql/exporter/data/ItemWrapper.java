package com.github.dcysteine.nesql.exporter.data;

import com.google.auto.value.AutoValue;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Optional;

/** Wrapper for items that supports hashing. */
@AutoValue
public abstract class ItemWrapper {
    public static ItemWrapper create(ItemStack itemStack) {
        return new AutoValue_ItemWrapper(
                itemStack.getItem(), itemStack.getItemDamage(),
                Optional.ofNullable(itemStack.stackTagCompound));
    }

    public abstract Item item();
    public abstract int damage();

    /**
     * {@link NBTTagCompound} is mutable! Be sure not to mutate the value returned here!
     *
     * <p>We are providing direct access to this mutable value to avoid the overhead of making a
     * copy, and because the limited scope of this mod means that it's unlikely that we would
     * accidentally mutate this.
     */
    public abstract Optional<NBTTagCompound> nbt();

    public ItemStack stack() {
        ItemStack itemStack = new ItemStack(item(), 1, damage());
        nbt().ifPresent(nbt -> itemStack.stackTagCompound = nbt);
        return itemStack;
    }
}
