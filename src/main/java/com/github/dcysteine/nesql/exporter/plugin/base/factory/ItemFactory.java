package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.exporter.util.render.RenderDispatcher;
import com.github.dcysteine.nesql.exporter.util.render.RenderJob;
import com.github.dcysteine.nesql.sql.base.Item;
import com.google.common.base.Joiner;
import cpw.mods.fml.common.registry.GameRegistry;
import jakarta.persistence.EntityManager;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class ItemFactory extends EntityFactory<Item, String> {
    private final Set<String> renderedItems;

    public ItemFactory(EntityManager entityManager) {
        super(entityManager);
        renderedItems = new HashSet<>();
    }

    public Item getItem(ItemStack itemStack) {
        Item item = new Item(
                IdUtil.itemId(itemStack),
                IdUtil.imageFilePath(itemStack),
                IdUtil.modId(itemStack),
                GameRegistry.findUniqueIdentifierFor(itemStack.getItem()).name,
                itemStack.getUnlocalizedName(),
                itemStack.getDisplayName(),
                net.minecraft.item.Item.getIdFromItem(itemStack.getItem()),
                itemStack.getItemDamage(),
                itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : null,
                Joiner.on('\n').join(
                        itemStack.getTooltip(Minecraft.getMinecraft().thePlayer, true)));

        if (ConfigOptions.RENDER_ICONS.get() && renderedItems.add(item.getId())) {
            RenderDispatcher.INSTANCE.addJob(RenderJob.ofItem(itemStack));
        }
        return findOrPersist(Item.class, item);
    }
}
