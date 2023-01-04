package com.github.dcysteine.nesql.sql.base.fluid;

import com.github.dcysteine.nesql.sql.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FluidGroupRepository extends Repository<FluidGroup, String> {
    @Query(value = "SELECT * FROM FLUIDGROUP WHERE ID IN ("
            + "SELECT FLUIDGROUP_ID FROM FLUIDGROUP_FLUIDSTACKS WHERE FLUID_ID = ?1)",
            nativeQuery = true)
    List<FluidGroup> findByFluid(String fluidId);
}
