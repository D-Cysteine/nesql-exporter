package com.github.dcysteine.nesql.exporter.plugin.mobsinfo;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.kuba6000.mobsinfo.loader.MobRecipeLoader;

import java.util.Map;

public class MobsInfoProcessor extends PluginHelper {
    public MobsInfoProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        Map<String, MobRecipeLoader.GeneralMappedMob> mobInfoMap = MobRecipeLoader.GeneralMobList;
        int total = mobInfoMap.size();
        logger.info("Processing {} mob info...", total);

        MobInfoFactory mobInfoFactory = new MobInfoFactory(exporter);
        int count = 0;
        for (Map.Entry<String, MobRecipeLoader.GeneralMappedMob> entry : mobInfoMap.entrySet()) {
            count++;

            mobInfoFactory.get(entry.getKey(), entry.getValue());

            if (Logger.intermittentLog(count)) {
                logger.info("Processed mob info {} of {}", count, total);
                logger.info("Most recent mob info: {}", entry.getKey());
            }
        }

        exporterState.flushEntityManager();
        logger.info("Finished processing mob info!");
    }
}
