package com.phoboss.telemetry.printer;

public enum PrinterType {
    POSITION("P"),
    SPEED("S"),
    LEAN("L");


    private final String value;

    PrinterType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
