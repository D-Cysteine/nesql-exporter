package com.github.dcysteine.nesql.exporter.plugin.thaumcraft.postprocessor;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.plugin.thaumcraft.factory.AspectFactory;
import jakarta.persistence.EntityManager;

import java.util.Collection;

public class AspectPostProcessor {
    private final EntityManager entityManager;

    public AspectPostProcessor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void postProcess() {
        Collection<thaumcraft.api.aspects.Aspect> aspects =
                thaumcraft.api.aspects.Aspect.aspects.values();
        int total = aspects.size();
        Logger.THAUMCRAFT.info("Post-processing {} aspects...", total);

        AspectFactory aspectFactory = new AspectFactory(entityManager);
        int count = 0;
        for (thaumcraft.api.aspects.Aspect aspect : aspects) {
            count++;
            aspectFactory.setComponents(aspect);

            if (Logger.intermittentLog(count)) {
                Logger.THAUMCRAFT.info("Post-processed aspect {} of {}", count, total);
                Logger.THAUMCRAFT.info("Most recent aspect: {}", aspect.getName());
            }
        }

        Logger.THAUMCRAFT.info("Finished post-processing aspects!");
    }
}
