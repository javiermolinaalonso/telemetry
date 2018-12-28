package com.javislaptop.telemetry.printer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ConsolePrinter {

    private static final Logger logger = LoggerFactory.getLogger(ConsolePrinter.class);
    private static final String SEPARATOR = "   ";

    private final Printer printer;

    public ConsolePrinter(Printer printer) {
        this.printer = printer;
    }

    @Scheduled(fixedRate = 5000, initialDelay = 1500)
    void printToConsole() {
        logger.info(printer.getEvents(SEPARATOR));
    }


}
