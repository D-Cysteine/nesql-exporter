package com.github.dcysteine.nesql.exporter.plugin.thaumcraft.processor;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.thaumcraft.factory.AspectFactory;
import com.github.dcysteine.nesql.sql.thaumcraft.Aspect;
import jakarta.persistence.EntityManager;

import java.util.Collection;

public class AspectProcessor {
    private final EntityManager entityManager;

    public AspectProcessor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void process() {
        Collection<thaumcraft.api.aspects.Aspect> aspects =
                thaumcraft.api.aspects.Aspect.aspects.values();
        int total = aspects.size();
        Logger.THAUMCRAFT.info("Processing {} aspects...", total);

        AspectFactory aspectFactory = new AspectFactory(entityManager);
        int count = 0;
        for (thaumcraft.api.aspects.Aspect aspect : aspects) {
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
