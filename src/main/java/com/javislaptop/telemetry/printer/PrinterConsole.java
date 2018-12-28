package com.javislaptop.telemetry.printer;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.time.ZoneOffset.UTC;

@Service
public class PrinterConsole implements Printer {

    private final ConcurrentMap<PrinterType, String> lastElements;

    private PrinterConsole() {
        lastElements = new ConcurrentHashMap<>();
    }

    @Override
    public void print(String text, PrinterType type) {
        lastElements.put(type, text);
    }

    @Override
    public String getEvents(String separator) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s%s", Instant.now().atZone(UTC), separator));
        lastElements.forEach((type, text) -> sb.append(String.format("%s#%s%s", type.getValue(), text, separator)));
        return sb.toString();
    }

}
