package com.javislaptop.telemetry.printer;

import java.text.DecimalFormat;

public interface Printer {

    DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    DecimalFormat LAT_LNG_FORMAT = new DecimalFormat("#.########");

    void print(String text, PrinterType type);

}
