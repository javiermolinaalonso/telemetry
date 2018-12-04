package com.javislaptop.telemetry.service;

import com.javislaptop.io.gps.GPSEvent;
import com.javislaptop.io.gps.GPSListener;
import com.javislaptop.io.gps.GpsDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GpsDataRetriever {

    private final GpsDataProvider gpsDataProvider;
    private List<GPSListener> listeners;

    @Autowired
    public GpsDataRetriever(GpsDataProvider gpsDataProvider) {
        this(gpsDataProvider, new ArrayList<>());
    }

    private GpsDataRetriever(GpsDataProvider gpsDataProvider, List<GPSListener> listeners) {
        this.gpsDataProvider = gpsDataProvider;
        this.listeners = listeners;
    }

    @Scheduled(fixedRate = 100)
    void pollGps() {
        gpsDataProvider.provide().forEach(this::parseAndNotifyListeners);
    }

    private void parseAndNotifyListeners(GPSEvent gpsEvent) {
        listeners.forEach(listener -> listener.onEvent(gpsEvent));
    }
    public void addListener(GPSListener gpsListener) {
        this.listeners.add(gpsListener);
    }

}
