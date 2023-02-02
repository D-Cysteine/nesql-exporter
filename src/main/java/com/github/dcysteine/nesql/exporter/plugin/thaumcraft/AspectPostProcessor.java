package com.github.dcysteine.nesql.exporter.plugin.thaumcraft;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.thaumcraft.factory.AspectFactory;

import java.util.Collection;

public class AspectPostProcessor extends PluginHelper {
    public AspectPostProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void postProcess() {
        Collection<thaumcraft.api.aspects.Aspect> aspects =
                thaumcraft.api.aspects.Aspect.aspects.values();
        int total = aspects.size();
        logger.info("Post-processing {} aspects...", total);

        AspectFactory aspectFactory = new AspectFactory(exporter);
        int count = 0;
        for (thaumcraft.api.aspects.Aspect aspect : aspects) {
            count++;
            aspectFactory.setComponents(aspect);

            if (Logger.intermittentLog(count)) {
                logger.info("Post-processed aspect {} of {}", count, total);
                logger.info("Most recent aspect: {}", aspect.getName());
            }
        }

        logger.info("Finished post-processing aspects!");
    }
}
