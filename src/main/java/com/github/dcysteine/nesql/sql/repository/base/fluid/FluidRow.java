package com.github.dcysteine.nesql.sql.repository.base.fluid;

import com.github.dcysteine.nesql.sql.util.IdUtil;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "fluid")
@Table(name = "fluid")
public class FluidRow {
    /**
     * This is the unique table key, NOT the Forge fluid ID! The latter is not unique (there can be
     * multiple fluid rows for the same Forge fluid ID).
     */
    @Id
    private String id;

    @Column(nullable = false)
    private String imageFileName;

    @Column(nullable = false)
    private String unlocalizedName;

    @Column(nullable = false)
    private String localizedName;

    /**
     * The Forge fluid ID.
     */
    @Column
    private int fluidId;

    // Hmm, I wonder if this is long enough?
    @Column(length = 2048)
    private String nbt;

    /** Needed by Hibernate. */
    protected FluidRow() {}

    public FluidRow(FluidStack fluidStack) {
        this.id = IdUtil.fluidId(fluidStack);
        this.imageFileName = IdUtil.fluidId(fluidStack.getFluid());
        this.unlocalizedName = fluidStack.getUnlocalizedName();
        this.localizedName = fluidStack.getLocalizedName();
        this.fluidId = fluidStack.getFluidID();
        this.nbt = fluidStack.tag == null ? null : fluidStack.tag.toString();
    }

    /**
     * This is the unique table key, NOT the Minecraft item ID! The latter is not unique (there can
     * be multiple item rows for the same Minecraft item ID).
     */
    public String getId() {
        return id;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    /** The Forge fluid ID. */
    public int getFluidId() {
        return fluidId;
    }

    @Nullable
    public String getNbt() {
        return nbt;
    }

    // TODO if needed, add toString, hashCode, compareTo, etc.
}
