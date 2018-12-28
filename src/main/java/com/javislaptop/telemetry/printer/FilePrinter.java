package com.javislaptop.telemetry.printer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class FilePrinter {

    private static final String COMMA_SEPARATOR = ",";
    private final Printer printer;
    @Value("${telemetry.output}")
    private String outputFile;

    public FilePrinter(Printer printer) {
        this.printer = printer;
    }

    @PostConstruct
    void initialize() throws IOException {
        new File(outputFile).createNewFile();
        printToFile("NEW TELEMETRY EXECUTION");
    }


    @Scheduled(fixedRate = 1000, initialDelay = 5000)
    void printToFile() throws IOException {
        printToFile(printer.getEvents(COMMA_SEPARATOR));
    }

    private void printToFile(String textToPrint) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, true));
        bw.newLine();
        bw.write(textToPrint);
        bw.close();
    }
}
