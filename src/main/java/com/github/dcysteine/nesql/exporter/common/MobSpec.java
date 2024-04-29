package com.github.dcysteine.nesql.exporter.common;

import com.github.dcysteine.nesql.exporter.main.Logger;
import com.google.auto.value.AutoValue;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Contains the data needed to specify a mob, along with some convenience methods.
 *
 * <p>Well, really this specifies a Minecraft entity.
 * But we'll use the term "mob" because "entity" is already used to refer to SQL entities.
 *
 * <p>Note: this class doesn't quite conform to the {@link AutoValue} contract, as some of its
 * contents ({@link NBTTagCompound}) are mutable. Shouldn't matter for our limited use-case, though.
 */
@AutoValue
public abstract class MobSpec {
    public static MobSpec create(String mobName) {
        return create(mobName, null);
    }

    public static MobSpec create(String mobName, @Nullable NBTTagCompound nbt) {
        String modId = "minecraft";
        String shortName = mobName;

        int separator = mobName.indexOf('.');
        if (separator > 0) {
            modId = mobName.substring(0, separator);
            shortName = mobName.substring(separator + 1);
        }
        // If separator == -1, then it's a vanilla mob; no mod ID in name

        return new AutoValue_MobSpec(modId, shortName, mobName, Optional.ofNullable(nbt));
    }

    public abstract String getModId();

    /** The internal name, with mod ID removed. This is equal to the full name for vanilla mobs. */
    public abstract String getShortName();

    /** The full internal name, including mod ID. Used to create the mob. */
    public abstract String getFullName();

    public abstract Optional<NBTTagCompound> getNbt();

    public Optional<Entity> createEntity() {
        try {
            Entity entity =
                    EntityList.createEntityByName(getFullName(), Minecraft.getMinecraft().theWorld);
            if (entity == null) {
                Logger.MOD.warn("Got null while creating entity: {}", this);
                return Optional.empty();
            }

            getNbt().ifPresent(entity::readFromNBT);
            return Optional.of(entity);
        } catch (Exception e) {
            Logger.MOD.error("Caught exception while creating entity: {}", this);
            e.printStackTrace();

            return Optional.empty();
        }
    }
}
