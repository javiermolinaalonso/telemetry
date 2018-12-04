package com.javislaptop.telemetry.printer;

import java.text.DecimalFormat;

public interface Printer {

    DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.00");
    DecimalFormat LAT_LNG_FORMAT = new DecimalFormat("#.00000000");

    void print(String text, PrinterType type);

}
