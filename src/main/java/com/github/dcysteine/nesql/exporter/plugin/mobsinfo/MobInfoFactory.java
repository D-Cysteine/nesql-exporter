package com.github.dcysteine.nesql.exporter.plugin.mobsinfo;

import com.github.dcysteine.nesql.exporter.common.MobSpec;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.MobFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.mob.Mob;
import com.github.dcysteine.nesql.sql.mobinfo.MobInfo;
import com.kuba6000.mobsinfo.api.SpawnInfo;
import com.kuba6000.mobsinfo.loader.MobRecipeLoader;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MobInfoFactory extends EntityFactory<MobInfo, String> {
    private final MobFactory mobFactory;
    private final MobDropFactory mobDropFactory;

    public MobInfoFactory(PluginExporter exporter) {
        super(exporter);
        this.mobFactory = new MobFactory(exporter);
        this.mobDropFactory = new MobDropFactory(exporter);
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

        // No need to actually do anything with the returned MobDrop objects,
        // because the MobDrop has ownership of the bidirectional MobInfo <-> MobDrop link.
        for (int i = 0; i < info.drops.size(); i++) {
            mobDropFactory.get(mobInfo, i, info.drops.get(i));
        }

        return findOrPersist(MobInfo.class, mobInfo);
    }
}
