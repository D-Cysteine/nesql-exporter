package com.github.dcysteine.nesql.exporter.util;

import java.text.NumberFormat;

public class NumberUtil {
    // Static class.
    private NumberUtil() {}

    public static String formatInteger(long integer) {
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        return numberFormat.format(integer);
    }
}
