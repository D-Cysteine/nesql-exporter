package com.github.dcysteine.nesql.exporter.plugin.mobsinfo;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.item.ItemStack;
import com.github.dcysteine.nesql.sql.mobinfo.MobDrop;
import com.github.dcysteine.nesql.sql.mobinfo.MobDropType;
import com.github.dcysteine.nesql.sql.mobinfo.MobInfo;

public class MobDropFactory extends EntityFactory<MobDrop, String> {
    private final ItemFactory itemFactory;

    private static MobDropType convertDropType(
            com.kuba6000.mobsinfo.api.MobDrop.DropType dropType) {
        switch (dropType) {
            case Normal:
                return MobDropType.NORMAL;

            case Rare:
                return MobDropType.RARE;

            case Additional:
                return MobDropType.ADDITIONAL;

            case Infernal:
                return MobDropType.INFERNAL;

            default:
                throw new IllegalArgumentException("Unhandled drop type: " + dropType);
        }
    }

    public MobDropFactory(PluginExporter exporter) {
        super(exporter);
        this.itemFactory = new ItemFactory(exporter);
    }

    public MobDrop get(
            String mobName, MobInfo mobInfo, int dropIndex,
            com.kuba6000.mobsinfo.api.MobDrop drop) {
        MobDropType dropType = convertDropType(drop.type);
        net.minecraft.item.ItemStack itemStack = drop.stack;
        Item item = itemFactory.get(itemStack);
        String id =
                IdPrefixUtil.MOB_DROP.applyPrefix(
                        IdUtil.mobId(mobName), Integer.toString(dropIndex), dropType.getName(),
                        item.getId());

        MobDrop mobDrop = new MobDrop(
                id,
                mobInfo,
                dropType,
                new ItemStack(item, itemStack.stackSize),
                drop.chance / 100_00d,
                drop.lootable,
                drop.playerOnly);

        entityManager.persist(mobDrop);
        return mobDrop;
    }
}
