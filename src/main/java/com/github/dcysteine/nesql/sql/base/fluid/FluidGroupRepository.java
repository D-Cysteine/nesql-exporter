package com.github.dcysteine.nesql.sql.base.fluid;

import com.github.dcysteine.nesql.sql.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FluidGroupRepository extends Repository<FluidGroup, String> {
    @Query(value = "SELECT * FROM FLUID_GROUP WHERE ID IN ("
            + "SELECT FLUID_GROUP_ID FROM FLUID_GROUP_FLUID_STACKS"
            + " WHERE FLUID_STACKS_FLUID_ID = ?1)",
            nativeQuery = true)
    List<FluidGroup> findByFluid(String fluidId);
}
