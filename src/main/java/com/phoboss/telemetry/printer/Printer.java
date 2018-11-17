package com.phoboss.telemetry.printer;

import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
public class Printer {

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    public static final DecimalFormat LAT_LNG_FORMAT = new DecimalFormat("#.########");

    private Printer() {
    }

    public void print(String text, PrinterType type) {
        System.out.println(String.format("%s#%s", type.getValue(), text));
    }
}
