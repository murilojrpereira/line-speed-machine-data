package com.fp.machinedata.control.common.utils;

import java.text.DecimalFormat;

public class ConvertDoubleValues {

    public static double formatDoubleTo(final double value) {
        DecimalFormat formatter = new DecimalFormat("#.##");
        return Double.parseDouble(formatter.format(value));
    }
}
