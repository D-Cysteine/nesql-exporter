package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.exporter.util.render.RenderDispatcher;
import com.github.dcysteine.nesql.exporter.util.render.RenderJob;
import com.github.dcysteine.nesql.sql.base.mob.Mob;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;

public class MobFactory extends EntityFactory<Mob, String> {
    public MobFactory(PluginExporter exporter) {
        super(exporter);
    }

    public Mob get(String mobName) {
        String id = IdPrefixUtil.MOB.applyPrefix(IdUtil.mobId(mobName));
        Mob mob = entityManager.find(Mob.class, id);
        if (mob != null) {
            return mob;
        }

        String modId = "minecraft";
        String internalName = mobName;
        int separator = mobName.indexOf('.');
        if (separator >= 0) {
            modId = mobName.substring(0, separator);
            internalName = mobName.substring(separator + 1);
        }

        Entity entity = null;
        try {
            entity = EntityList.createEntityByName(mobName, Minecraft.getMinecraft().theWorld);
        } catch (Exception e) {
            logger.error("Caught exception while trying to create entity: {}", mobName);
            e.printStackTrace();
        }

        if (entity == null) {
            logger.warn("Got null when creating entity: {}", mobName);

            mob = new Mob(
                    id,
                    StringUtil.formatFilePath(IdUtil.mobImageFilePath(mobName)),
                    modId,
                    internalName,
                    "ERROR",
                    0f,
                    0f,
                    0d,
                    0,
                    false,
                    false);

        } else if (!(entity instanceof EntityLiving)) {
            // How much of a problem is this, though?
            // We should be able to render it just fine... I think
            logger.warn("MobFactory called on non-living entity: {}", mobName);

            mob = new Mob(
                    id,
                    StringUtil.formatFilePath(IdUtil.mobImageFilePath(mobName)),
                    modId,
                    internalName,
                    entity.getCommandSenderName(),
                    entity.width,
                    entity.height,
                    0d,
                    0,
                    entity.isImmuneToFire(),
                    false);

        } else {
            EntityLiving entityLiving = (EntityLiving) entity;

            mob = new Mob(
                    id,
                    StringUtil.formatFilePath(IdUtil.mobImageFilePath(mobName)),
                    modId,
                    internalName,
                    entityLiving.getCommandSenderName(),
                    entityLiving.width,
                    entityLiving.height,
                    entityLiving.getMaxHealth(),
                    entityLiving.getTotalArmorValue(),
                    entityLiving.isImmuneToFire(),
                    entityLiving.allowLeashing());
        }

        if (entity != null && ConfigOptions.RENDER_MOBS.get()) {
            Logger.intermittentLog(
                    logger,
                    "Enqueueing render of mob #{}: " + mobName,
                    exporterState.incrementMobCount());
            RenderDispatcher.INSTANCE.addJob(RenderJob.ofMobName(mobName));
        }

        entityManager.persist(mob);
        return mob;
    }
}
