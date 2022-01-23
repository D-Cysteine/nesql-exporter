package com.github.dcysteine.nesql.exporter.handler.forestry;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.toprettystring.ToPrettyString;
import com.google.common.base.Preconditions;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@AutoValue
public abstract class ItemStackWrapper {
    public static ItemStackWrapper create(ItemStack itemStack) {
        Preconditions.checkArgument(itemStack.getTagCompound() == null);
        Preconditions.checkArgument(!itemStack.isItemStackDamageable());

        return new AutoValue_ItemStackWrapper(
                itemStack.getDisplayName(), itemStack.getItem(), itemStack.getItemDamage());
    }

    public abstract String name();
    public abstract Item item();
    public abstract int damage();

    public final boolean equalsItemStack(ItemStack itemStack) {
        return item() == itemStack.getItem() && damage() == itemStack.getItemDamage();
    }

    @ToPrettyString
    @Override
    public abstract String toString();
}
