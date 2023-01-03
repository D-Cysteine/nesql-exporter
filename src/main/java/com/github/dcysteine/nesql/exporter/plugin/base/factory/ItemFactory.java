package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.exporter.util.ItemUtil;
import com.github.dcysteine.nesql.exporter.util.render.RenderDispatcher;
import com.github.dcysteine.nesql.exporter.util.render.RenderJob;
import com.github.dcysteine.nesql.exporter.util.render.Renderer;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.google.common.base.Joiner;
import cpw.mods.fml.common.registry.GameRegistry;
import jakarta.persistence.EntityManager;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ItemFactory extends EntityFactory<Item, String> {
    public ItemFactory(EntityManager entityManager) {
        super(entityManager);
    }

    public Item getItem(ItemStack itemStack) {
        Item item;
        try {
            item = new Item(
                    IdUtil.itemId(itemStack),
                    IdUtil.imageFilePath(itemStack),
                    IdUtil.modId(itemStack),
                    GameRegistry.findUniqueIdentifierFor(itemStack.getItem()).name,
                    itemStack.getUnlocalizedName(),
                    itemStack.getDisplayName(),
                    ItemUtil.getItemId(itemStack),
                    itemStack.getItemDamage(),
                    itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : null,
                    Joiner.on('\n').join(
                            itemStack.getTooltip(Minecraft.getMinecraft().thePlayer, true)));
        } catch (Exception e) {
            // Sometimes items will throw exceptions when you try to get their name or tooltip.
            item = new Item(
                    IdUtil.itemId(itemStack),
                    IdUtil.imageFilePath(itemStack),
                    IdUtil.modId(itemStack),
                    GameRegistry.findUniqueIdentifierFor(itemStack.getItem()).name,
                    "ERROR",
                    "ERROR",
                    ItemUtil.getItemId(itemStack),
                    itemStack.getItemDamage(),
                    itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : null,
                    Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString)
                            .collect(Collectors.joining("\n")));
            Logger.MOD.error("Caught exception while trying to persist item: {}", item.getId());
            e.printStackTrace();
        }

        if (Renderer.INSTANCE.isUnrenderedItem(item.getId())
                && ConfigOptions.RENDER_ICONS.get()) {
            if (Logger.intermittentLog(
                    "Enqueueing render of item #{}: " + item.getId(),
                    Renderer.INSTANCE.getRenderedItemCount())) {
                Logger.MOD.info(
                        "Remaining render jobs: " + RenderDispatcher.INSTANCE.getJobCount());
            }

            RenderDispatcher.INSTANCE.addJob(RenderJob.ofItem(itemStack));
        }

        return findOrPersist(Item.class, item);
    }
}
