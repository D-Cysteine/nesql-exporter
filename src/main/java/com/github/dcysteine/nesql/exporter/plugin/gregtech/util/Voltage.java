package com.github.dcysteine.nesql.exporter.plugin.gregtech.util;

import com.google.common.collect.ImmutableMap;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Enum holding valid GregTech voltages. This will need to be manually updated, should we ever add
 * any voltage tiers.
 */
public enum Voltage {
    ULV(0),
    LV(1),
    MV(2),
    HV(3),
    EV(4),
    IV(5),
    LuV(6),
    ZPM(7),
    UV(8),
    UHV(9),
    UEV(10),
    UIV(11),
    UMV(12),
    UXV(13),
    MAX(14),
    ;

    private final int index;
    private final String name;

    Voltage(int index) {
        this.index = index;
        this.name = GT_Values.VN[index];
    }

    private static final ImmutableMap<Integer, Voltage> VOLTAGE_MAP =
            ImmutableMap.copyOf(
                    Arrays.stream(values())
                            .collect(Collectors.toMap(Voltage::getIndex, Function.identity())));


    public static Voltage getVoltage(int index) {
        return VOLTAGE_MAP.get(index);
    }

    public static Voltage convertVoltage(long voltage) {
        return VOLTAGE_MAP.get((int) GT_Utility.getTier(voltage));
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }
}
