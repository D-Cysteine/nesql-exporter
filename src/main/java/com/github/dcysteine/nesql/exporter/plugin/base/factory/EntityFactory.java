package com.github.dcysteine.nesql.exporter.plugin.base.factory;

import bq_standard.tasks.TaskHunt;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.exporter.util.IdUtil;
import com.github.dcysteine.nesql.exporter.util.StringUtil;
import com.github.dcysteine.nesql.exporter.util.render.RenderDispatcher;
import com.github.dcysteine.nesql.exporter.util.render.RenderJob;
import com.github.dcysteine.nesql.sql.base.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;

public class EntityFactory extends com.github.dcysteine.nesql.exporter.plugin.EntityFactory<Entity, String> {
    public EntityFactory(PluginExporter exporter) {
        super(exporter);
    }

    public Entity get(TaskHunt taskHunt) {
        net.minecraft.entity.Entity target = null;
        if (EntityList.stringToClassMapping.containsKey(taskHunt.idName)) {
            target = EntityList.createEntityByName(taskHunt.idName, Minecraft.getMinecraft().theWorld);
            if (target != null) target.readFromNBT(taskHunt.targetTags);
//            if (target != null && !taskHunt.targetTags.hasNoTags()) target.readFromNBT(taskHunt.targetTags);
        }
        return get(target);
    }
    public Entity get(net.minecraft.entity.Entity entity) {
        String id = IdPrefixUtil.ENTITY.applyPrefix(IdUtil.entityId(entity));
        Entity sqlEntity = entityManager.find(Entity.class, id);
        if (sqlEntity != null) {
            return sqlEntity;
        }

        String entityId = EntityList.getEntityString(entity);
        int firstIndex = entityId.indexOf('.');
        String modId, internalName;
        if (firstIndex > 0) {
            modId = entityId.substring(0, firstIndex);
            internalName = entityId.substring(firstIndex + 1);
        } else {
            modId = "minecraft";
            internalName = entityId;
        }

        String nbt = "";
        if (!entity.getEntityData().hasNoTags())
            nbt = entity.getEntityData().toString();

        sqlEntity = new Entity(
                id,
                StringUtil.formatFilePath(IdUtil.imageFilePath(entity)),
                modId,
                internalName,
                entityId,
                StringUtil.stripFormatting(entity.getCommandSenderName()),
                entity.getEntityId(),
                nbt);

        if (ConfigOptions.RENDER_ENTITIES.get()) {
            Logger.intermittentLog(
                    logger,
                    "Enqueueing render of entity #{}: " + entity.getCommandSenderName(),
                    exporterState.incrementEntityCount());
            RenderDispatcher.INSTANCE.addJob(RenderJob.ofEntity(entity));
        }

        entityManager.persist(sqlEntity);
        return sqlEntity;
    }
}
