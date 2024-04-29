package com.github.dcysteine.nesql.exporter.plugin.mobsinfo;

import com.github.dcysteine.nesql.exporter.common.MobSpec;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.MobFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.base.mob.Mob;
import com.github.dcysteine.nesql.sql.mobinfo.MobDrop;
import com.github.dcysteine.nesql.sql.mobinfo.MobDropType;
import com.github.dcysteine.nesql.sql.mobinfo.MobInfo;
import com.kuba6000.mobsinfo.api.SpawnInfo;
import com.kuba6000.mobsinfo.loader.MobRecipeLoader;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MobInfoFactory extends EntityFactory<MobInfo, String> {
    private final MobFactory mobFactory;
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

    public MobInfoFactory(PluginExporter exporter) {
        super(exporter);
        this.mobFactory = new MobFactory(exporter);
        this.itemFactory = new ItemFactory(exporter);
    }

    public MobInfo get(String mobName, MobRecipeLoader.GeneralMappedMob info) {
        Mob mob;
        if (mobName.equals("witherSkeleton")) {
            // MobsInfo has special handling for wither skeletons, which we must also handle here.
            mob = mobFactory.getWitherSkeleton();
        } else {
            mob = mobFactory.get(MobSpec.create(mobName));
        }
        String id = IdPrefixUtil.MOB_INFO.applyPrefix(mob.getId());

        Set<String> spawnInfo;
        if (info.recipe.spawnList == null) {
            spawnInfo = new HashSet<>();
        } else {
            spawnInfo =
                    info.recipe.spawnList.stream()
                            .map(SpawnInfo::getInfo)
                            .collect(Collectors.toSet());
        }

        MobInfo mobInfo = new MobInfo(
                id,
                mob,
                info.recipe.isPeacefulAllowed,
                info.recipe.isUsableInVial,
                info.recipe.infernalityAllowed,
                info.recipe.alwaysinfernal,
                spawnInfo);
        info.drops.forEach(d -> mobInfo.addDrop(buildDrop(d)));

        return findOrPersist(MobInfo.class, mobInfo);
    }

    public MobDrop buildDrop(com.kuba6000.mobsinfo.api.MobDrop drop) {
        MobDropType dropType = convertDropType(drop.type);
        net.minecraft.item.ItemStack itemStack = drop.stack;
        Item item = itemFactory.get(itemStack);

        return new MobDrop(
                dropType,
                item,
                itemStack.stackSize,
                drop.chance / 100_00d,
                drop.lootable,
                drop.playerOnly);
    }
}
