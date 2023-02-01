package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.plugin.Database;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.exporter.util.ItemUtil;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.exporter.util.render.RenderDispatcher;
import com.github.dcysteine.nesql.exporter.util.render.RenderJob;
import com.github.dcysteine.nesql.sql.base.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ItemFactory extends EntityFactory<Item, String> {
    public ItemFactory(Database database) {
        super(database);
    }

    public Item getItem(ItemStack itemStack) {
        String id = IdPrefixUtil.ITEM.applyPrefix(IdUtil.itemId(itemStack));
        Item item = entityManager.find(Item.class, id);
        if (item != null) {
            return item;
        }

        GameRegistry.UniqueIdentifier uniqueId =
                GameRegistry.findUniqueIdentifierFor(itemStack.getItem());
        String modId = uniqueId.modId;
        String internalName = uniqueId.name;

        // We're just exporting data, not actually doing recipe matching, so I think we can just
        // ignore wildcard NBT. It probably isn't handled by most recipe types, anyway.
        String nbt = "";
        if (itemStack.hasTagCompound() && !ItemUtil.isWildcardNbt(itemStack.getTagCompound())) {
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
                    itemStack.getItemDamage(),
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
                    itemStack.getItemDamage(),
                    nbt,
                    stackTrace,
                    itemStack.getMaxStackSize(),
                    itemStack.getMaxDamage(),
                    new HashMap<>());
            Logger.BASE.error("Caught exception while trying to persist item: {}", item.getId());
            e.printStackTrace();
        }

        if (ConfigOptions.RENDER_ICONS.get()) {
            Logger.intermittentLog(
                    Logger.BASE,
                    "Enqueueing render of item #{}: " + item.getLocalizedName(),
                    database.incrementItemCount());
            RenderDispatcher.INSTANCE.addJob(RenderJob.ofItem(itemStack));
        }

        entityManager.persist(item);
        database.invokeItemListeners(item, itemStack);
        return item;
    }
}
