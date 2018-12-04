package com.javislaptop.telemetry.printer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.time.ZoneOffset.UTC;

@Service
public class PrinterConsole implements Printer {

    private static final String SEPARATOR = "   ";

    private final ConcurrentMap<PrinterType, String> lastElements;

    private PrinterConsole() {
        lastElements = new ConcurrentHashMap<>();
    }

    @Override
    public void print(String text, PrinterType type) {
        lastElements.put(type, text);
    }

    @Scheduled(fixedRate = 500, initialDelay = 1500)
    void print() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s%s", Instant.now().atZone(UTC), SEPARATOR));
        lastElements.forEach((type, text) -> sb.append(String.format("%s#%s%s", type.getValue(), text, SEPARATOR)));
        System.out.println(sb.toString());
    }
}