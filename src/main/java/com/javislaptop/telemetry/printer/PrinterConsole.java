package com.javislaptop.telemetry.printer;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.lang.Thread.sleep;

@Service
public class PrinterConsole implements Printer {

    private final ConcurrentMap<PrinterType, String> lastElements;

    private PrinterConsole() {
        lastElements = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void tickerInit() {
        new Thread(() -> {
            while (true) {
                lastElements.forEach((type, text) -> System.out.print(String.format("%s#%s$", type.getValue(), text)));
                System.out.println();
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void print(String text, PrinterType type) {
        lastElements.put(type, text);
    }
}
