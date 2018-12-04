package com.javislaptop.telemetry.service;

import com.javislaptop.io.gps.GPSEvent;
import com.javislaptop.io.gps.GPSListener;
import com.javislaptop.io.gps.GpsDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GpsDataRetriever implements Runnable {

    private static final int DEFAULT_READ_INTERVAL = 550;
    private final GpsDataProvider gpsDataProvider;

    private volatile boolean isRunning = true;

    private List<GPSListener> listeners;

    @Autowired
    public GpsDataRetriever(GpsDataProvider gpsDataProvider) {
        this(gpsDataProvider, new ArrayList<>());
    }

    private GpsDataRetriever(GpsDataProvider gpsDataProvider, List<GPSListener> listeners) {
        this.gpsDataProvider = gpsDataProvider;
        this.listeners = listeners;
    }

    void init() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (isRunning) {
            gpsDataProvider.provide().forEach(this::parseAndNotifyListeners);
            sleep();
        }

        gpsDataProvider.shutdown();
    }

    private void parseAndNotifyListeners(GPSEvent gpsEvent) {
        listeners.forEach(listener -> listener.onEvent(gpsEvent));
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
