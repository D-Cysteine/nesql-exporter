package com.github.dcysteine.nesql.exporter.plugin.thaumcraft.processor;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.Database;
import com.github.dcysteine.nesql.exporter.plugin.thaumcraft.factory.AspectFactory;
import thaumcraft.api.aspects.Aspect;

import java.util.Collection;

public class AspectProcessor {
    private final Database database;

    public AspectProcessor(Database database) {
        this.database = database;
    }

    public void process() {
        Collection<Aspect> aspects = Aspect.aspects.values();
        int total = aspects.size();
        Logger.THAUMCRAFT.info("Processing {} aspects...", total);

        AspectFactory aspectFactory = new AspectFactory(database);
        int count = 0;
        for (Aspect aspect : aspects) {
            count++;
            aspectFactory.getAspect(aspect);

            if (Logger.intermittentLog(count)) {
                Logger.THAUMCRAFT.info("Processed aspect {} of {}", count, total);
                Logger.THAUMCRAFT.info("Most recent aspect: {}", aspect.getName());
            }
        }

        Logger.THAUMCRAFT.info("Finished processing aspects!");
    }
}
