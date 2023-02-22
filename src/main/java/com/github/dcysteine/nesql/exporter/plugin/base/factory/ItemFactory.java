package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.exporter.util.ItemUtil;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.exporter.util.render.RenderDispatcher;
import com.github.dcysteine.nesql.exporter.util.render.RenderJob;
import com.github.dcysteine.nesql.sql.base.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ItemFactory extends EntityFactory<Item, String> {
    public ItemFactory(PluginExporter exporter) {
        super(exporter);
    }

    public Item get(net.minecraft.item.Item item) {
        return get(new ItemStack(item, 1));
    }

    public Item get(ItemStack itemStack) {
        String id = IdPrefixUtil.ITEM.applyPrefix(IdUtil.itemId(itemStack));
        Item item = entityManager.find(Item.class, id);
        if (item != null) {
            return item;
        }

        GameRegistry.UniqueIdentifier uniqueId =
                GameRegistry.findUniqueIdentifierFor(itemStack.getItem());
        String modId = uniqueId.modId;
        String internalName = uniqueId.name;

        // We can't just call itemStack.getItemDamage(), because that may have been overridden
        // to return something other than the raw ItemStack.itemDamage field.
        int itemDamage = Items.feather.getDamage(itemStack);

        String nbt = "";
        if (itemStack.hasTagCompound()) {
            nbt = itemStack.getTagCompound().toString();
        }

        try {
            @SuppressWarnings("unchecked")
            String tooltip =
                    ((List<String>) itemStack.getTooltip(Minecraft.getMinecraft().thePlayer, true))
                            .stream()
                            .map(StringUtil::stripFormatting)
                            .collect(Collectors.joining("\n"));

            item = new Item(
                    id,
                    StringUtil.formatFilePath(IdUtil.imageFilePath(itemStack)),
                    modId,
                    internalName,
                    itemStack.getUnlocalizedName(),
                    StringUtil.stripFormatting(itemStack.getDisplayName()),
                    ItemUtil.getItemId(itemStack),
                    itemDamage,
                    nbt,
                    tooltip,
                    itemStack.getMaxStackSize(),
                    itemStack.getMaxDamage(),
                    ItemUtil.getToolClasses(itemStack));
        } catch (Exception e) {
            // Sometimes items will throw exceptions when you try to get their name or tooltip.
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            String stackTrace = stringWriter.toString();

            item = new Item(
                    id,
                    StringUtil.formatFilePath(IdUtil.imageFilePath(itemStack)),
                    modId,
                    internalName,
                    "ERROR",
                    "ERROR",
                    ItemUtil.getItemId(itemStack),
                    itemDamage,
                    nbt,
                    stackTrace,
                    itemStack.getMaxStackSize(),
                    itemStack.getMaxDamage(),
                    new HashMap<>());
            logger.error("Caught exception while trying to persist item: {}", item.getId());
            e.printStackTrace();
        }

        if (ConfigOptions.RENDER_ICONS.get()) {
            Logger.intermittentLog(
                    logger,
                    "Enqueueing render of item #{}: " + item.getLocalizedName(),
                    exporterState.incrementItemCount());
            RenderDispatcher.INSTANCE.addJob(RenderJob.ofItem(itemStack));
        }

        entityManager.persist(item);
        exporterState.invokeItemListeners(item, itemStack);
        return item;
    }
}
