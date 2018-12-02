package com.javislaptop.io.gps.impl;

import com.javislaptop.io.gps.GPSListener;
import com.javislaptop.io.gps.GpsDataProvider;

import java.util.ArrayList;
import java.util.List;

public class GpsDataRetriever implements Runnable {

    private static final int DEFAULT_READ_INTERVAL = 550;
    private final GpsParser gpsParser;
    private final GpsDataProvider gpsDataProvider;

    private volatile boolean isRunning = true;

    private List<GPSListener> listeners;

    public GpsDataRetriever(GpsDataProvider gpsDataProvider) {
        this(new GpsParser(), gpsDataProvider, new ArrayList<>());
    }

    private GpsDataRetriever(GpsParser gpsParser, GpsDataProvider gpsDataProvider, List<GPSListener> listeners) {
        this.gpsParser = gpsParser;
        this.gpsDataProvider = gpsDataProvider;
        this.listeners = listeners;
    }

    @Override
    public void run() {
        while (isRunning) {
            gpsDataProvider.provide().ifPresent(this::parseAndNotifyListeners);
            sleep();
        }

        gpsDataProvider.shutdown();
    }

    private void parseAndNotifyListeners(String gpsData) {
        gpsParser.parse(gpsData).forEach(gpsEvent -> listeners.forEach(listener -> listener.onEvent(gpsEvent)));
    }

    private void sleep() {
        try {
            Thread.sleep(DEFAULT_READ_INTERVAL);
        } catch (InterruptedException e) {
        }
    }

    public void addListener(GPSListener gpsListener) {
        this.listeners.add(gpsListener);
    }

    public void shutdown() {
        isRunning = false;
    }
}
