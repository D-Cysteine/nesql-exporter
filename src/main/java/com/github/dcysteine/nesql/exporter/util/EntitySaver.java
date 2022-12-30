package com.github.dcysteine.nesql.exporter.util;

import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.util.render.RenderDispatcher;
import com.github.dcysteine.nesql.exporter.util.render.RenderJob;
import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.fluid.FluidRepository;
import com.github.dcysteine.nesql.sql.base.fluid.FluidRow;
import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
import com.github.dcysteine.nesql.sql.base.item.ItemRow;
import com.google.common.base.Joiner;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import cpw.mods.fml.common.registry.GameRegistry;
import joptsimple.internal.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/** Templated class that handles saving rows to tables. */
public class EntitySaver {
    private final EntityManager entityManager;
    private final Session session;
    private final Map<Class<?>, TableSaver<?, ?, ?>> tableMap;
    private final SetMultimap<Class<?>, Object> keyMultimap;
    private final HashSet<String> renderedFluids;

    public EntitySaver(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.session = entityManager.unwrap(Session.class);
        this.tableMap = new HashMap<>();
        this.keyMultimap = MultimapBuilder.SetMultimapBuilder.hashKeys().hashSetValues().build();
        this.renderedFluids = new HashSet<>();
    }

    /**
     * Do NOT call this method for items or fluids! Call the item- and fluid-specific methods
     * instead, as those contain extra logic for rendering.
     *
     * @return {@code true} if the item was newly saved; {@code false} if it was already present
     */
    // Unfortunately, I think our type shenanigans here are too complex for the compiler to follow.
    @SuppressWarnings("unchecked")
    public <T extends JpaRepository<R, K>, R extends Identifiable<K>, K> boolean save(
            Class<T> clazz, R row) {
        boolean newlySaved = keyMultimap.put(clazz, row.getId());
        if (newlySaved) {
            TableSaver<T, R, K> tableSaver =
                    (TableSaver<T, R, K>)
                            tableMap.computeIfAbsent(clazz, c -> new TableSaver<>(clazz));
            tableSaver.save(row);
        }
        return newlySaved;
    }

    public boolean saveItem(ItemStack itemStack) {
        boolean newlySaved = save(ItemRepository.class, buildItemRow(itemStack));
        if (newlySaved) {
            if (ConfigOptions.RENDER_ICONS.get()) {
                RenderDispatcher.INSTANCE.addJob(RenderJob.ofItem(itemStack));
            }
        }
        return newlySaved;
    }

    public boolean saveFluid(FluidStack fluidStack) {
        boolean newlySaved = save(FluidRepository.class, buildFluidRow(fluidStack));
        if (newlySaved) {
            String renderedFluidKey = IdUtil.fluidId(fluidStack.getFluid());
            if (ConfigOptions.RENDER_ICONS.get() && renderedFluids.add(renderedFluidKey)) {
                RenderDispatcher.INSTANCE.addJob(RenderJob.ofFluid(fluidStack));
            }
        }
        return newlySaved;
    }

    private ItemRow buildItemRow(ItemStack itemStack) {
        return new ItemRow(
                IdUtil.itemId(itemStack),
                IdUtil.imageFilePath(itemStack),
                IdUtil.modId(itemStack),
                GameRegistry.findUniqueIdentifierFor(itemStack.getItem()).name,
                itemStack.getUnlocalizedName(),
                itemStack.getDisplayName(),
                Item.getIdFromItem(itemStack.getItem()),
                itemStack.getItemDamage(),
                itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : null,
                Joiner.on('\n').join(
                        itemStack.getTooltip(Minecraft.getMinecraft().thePlayer, true)));
    }

    private FluidRow buildFluidRow(FluidStack fluidStack) {
        return new FluidRow(
                IdUtil.fluidId(fluidStack),
                IdUtil.imageFilePath(fluidStack),
                fluidStack.getFluid().getName(),
                fluidStack.getUnlocalizedName(),
                fluidStack.getLocalizedName(),
                fluidStack.getFluidID(),
                fluidStack.tag == null ? null : fluidStack.tag.toString());
    }

    private class TableSaver<T extends JpaRepository<R, K>, R extends Identifiable<K>, K> {
        private final T repository;

        private TableSaver(Class<T> clazz) {
            repository = new JpaRepositoryFactory(entityManager).getRepository(clazz);
        }

        private void save(R row) {
            session.beginTransaction();
            repository.save(row);
            session.getTransaction().commit();
        }
    }
}
