package com.github.dcysteine.nesql.exporter.util;

import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.util.render.RenderDispatcher;
import com.github.dcysteine.nesql.exporter.util.render.RenderJob;
import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.fluid.FluidRepository;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.item.ItemRepository;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.google.common.base.Joiner;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.hibernate.Session;
import org.springframework.data.repository.CrudRepository;

import jakarta.persistence.EntityManager;
import java.util.HashSet;

/** Templated class that handles saving rows to tables. */
public class EntitySaver {
    private final EntityManager entityManager;
    private final Session session;
    private final SetMultimap<Class<?>, Object> keyMultimap;
    private final HashSet<String> renderedFluids;

    public EntitySaver(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.session = entityManager.unwrap(Session.class);
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
    public <T extends CrudRepository<R, K>, R extends Identifiable<K>, K> boolean save(
            Class<T> clazz, R row) {
        boolean newlySaved = keyMultimap.put(clazz, row.getId());
        if (newlySaved) {
            session.beginTransaction();
            entityManager.persist(row);
            session.getTransaction().commit();
        }
        return newlySaved;
    }

    public boolean saveItem(ItemStack itemStack) {
        boolean newlySaved = save(ItemRepository.class, buildItem(itemStack));
        if (newlySaved) {
            if (ConfigOptions.RENDER_ICONS.get()) {
                RenderDispatcher.INSTANCE.addJob(RenderJob.ofItem(itemStack));
            }
        }
        return newlySaved;
    }

    public boolean saveFluid(FluidStack fluidStack) {
        boolean newlySaved = save(FluidRepository.class, buildFluid(fluidStack));
        if (newlySaved) {
            String renderedFluidKey = IdUtil.fluidId(fluidStack.getFluid());
            if (ConfigOptions.RENDER_ICONS.get() && renderedFluids.add(renderedFluidKey)) {
                RenderDispatcher.INSTANCE.addJob(RenderJob.ofFluid(fluidStack));
            }
        }
        return newlySaved;
    }

    private Item buildItem(ItemStack itemStack) {
        return new Item(
                IdUtil.itemId(itemStack),
                IdUtil.imageFilePath(itemStack),
                IdUtil.modId(itemStack),
                GameRegistry.findUniqueIdentifierFor(itemStack.getItem()).name,
                itemStack.getUnlocalizedName(),
                itemStack.getDisplayName(),
                net.minecraft.item.Item.getIdFromItem(itemStack.getItem()),
                itemStack.getItemDamage(),
                itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : null,
                Joiner.on('\n').join(
                        itemStack.getTooltip(Minecraft.getMinecraft().thePlayer, true)));
    }

    private Fluid buildFluid(FluidStack fluidStack) {
        return new Fluid(
                IdUtil.fluidId(fluidStack),
                IdUtil.imageFilePath(fluidStack),
                fluidStack.getFluid().getName(),
                fluidStack.getUnlocalizedName(),
                fluidStack.getLocalizedName(),
                fluidStack.getFluidID(),
                fluidStack.tag == null ? null : fluidStack.tag.toString());
    }
}
