package com.github.dcysteine.nesql.exporter.plugin.minecraft;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.MobFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;

import java.util.Map;

public class EntityProcessor extends PluginHelper {
    public EntityProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        @SuppressWarnings("unchecked")
        Map<String, Class<Entity>> entityMap = EntityList.stringToClassMapping;
        int total = entityMap.size();
        logger.info("Processing {} entities...", total);

        MobFactory mobFactory = new MobFactory(exporter);
        int count = 0;
        for (Map.Entry<String, Class<Entity>> entry : entityMap.entrySet()) {
            count++;

            String entityName = entry.getKey();
            Class<Entity> clazz = entry.getValue();
            if (clazz != null && EntityLiving.class.isAssignableFrom(clazz)) {
                mobFactory.get(entityName);
            }

            if (Logger.intermittentLog(count)) {
                logger.info("Processed entity {} of {}", count, total);
                logger.info("Most recent entity: {}", entityName);
            }
        }

        // TODO maybe manually include wither skeleton and charged creeper? Need NBT support for it

        exporterState.flushEntityManager();
        logger.info("Finished processing entities!");
    }
}
