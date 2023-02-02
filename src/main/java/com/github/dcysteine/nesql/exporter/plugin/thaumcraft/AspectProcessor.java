package com.github.dcysteine.nesql.exporter.plugin.thaumcraft;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.thaumcraft.factory.AspectFactory;
import thaumcraft.api.aspects.Aspect;

import java.util.Collection;

public class AspectProcessor extends PluginHelper {

    public AspectProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        Collection<Aspect> aspects = Aspect.aspects.values();
        int total = aspects.size();
        logger.info("Processing {} aspects...", total);

        AspectFactory aspectFactory = new AspectFactory(exporter);
        int count = 0;
        for (Aspect aspect : aspects) {
            count++;
            aspectFactory.getAspect(aspect);

            if (Logger.intermittentLog(count)) {
                logger.info("Processed aspect {} of {}", count, total);
                logger.info("Most recent aspect: {}", aspect.getName());
            }
        }

        logger.info("Finished processing aspects!");
    }
}
