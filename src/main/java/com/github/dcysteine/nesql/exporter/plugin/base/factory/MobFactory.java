package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import com.github.dcysteine.nesql.exporter.common.MobSpec;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.render.RenderDispatcher;
import com.github.dcysteine.nesql.exporter.render.RenderJob;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.sql.base.mob.Mob;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Optional;

public class MobFactory extends EntityFactory<Mob, String> {
    public MobFactory(PluginExporter exporter) {
        super(exporter);
    }

    /**
     * Special case method that returns the {@link Mob} for a vanilla wither skeleton.
     *
     * <p>Wither skeletons are just regular skeletons with NBT, which makes them annoying to handle.
     */
    public Mob getWitherSkeleton() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("SkeletonType", (byte) 1);

        return get(MobSpec.create("Skeleton", nbt));
    }

    public Mob get(MobSpec spec) {
        String id = IdPrefixUtil.MOB.applyPrefix(IdUtil.mobId(spec));
        Mob mob = entityManager.find(Mob.class, id);
        if (mob != null) {
            return mob;
        }

        String nbt = "";
        if (spec.getNbt().isPresent()) {
            nbt = spec.getNbt().get().toString();
        }

        Optional<Entity> entityOptional = spec.createEntity();
        if (!entityOptional.isPresent()) {
            logger.warn("Could not create entity: {}", spec);

            mob = new Mob(
                    id,
                    StringUtil.formatFilePath(IdUtil.mobImageFilePath(spec)),
                    spec.getModId(),
                    spec.getFullName(),
                    "ERROR",
                    nbt,
                    0f,
                    0f,
                    0d,
                    0,
                    false,
                    false);

        } else {
            Entity entity = entityOptional.get();
            if (!(entity instanceof EntityLiving)) {
                // How much of a problem is this, though?
                // We should be able to render it just fine... I think
                logger.warn("MobFactory called on non-living entity: {}", spec);

                mob = new Mob(
                        id,
                        StringUtil.formatFilePath(IdUtil.mobImageFilePath(spec)),
                        spec.getModId(),
                        spec.getFullName(),
                        entity.getCommandSenderName(),
                        nbt,
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
                        StringUtil.formatFilePath(IdUtil.mobImageFilePath(spec)),
                        spec.getModId(),
                        spec.getFullName(),
                        entityLiving.getCommandSenderName(),
                        nbt,
                        entityLiving.width,
                        entityLiving.height,
                        entityLiving.getMaxHealth(),
                        entityLiving.getTotalArmorValue(),
                        entityLiving.isImmuneToFire(),
                        entityLiving.allowLeashing());
            }
        }

        if (entityOptional.isPresent() && ConfigOptions.RENDER_MOBS.get()) {
            Logger.intermittentLog(
                    logger,
                    "Enqueueing render of mob #{}: " + spec.getFullName(),
                    exporterState.incrementMobCount());
            RenderDispatcher.INSTANCE.addJob(RenderJob.ofMob(spec));
        }

        entityManager.persist(mob);
        return mob;
    }
}
