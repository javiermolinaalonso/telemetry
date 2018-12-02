package com.javislaptop.telemetry.service;

import com.javislaptop.io.gps.GPSEvent;
import com.javislaptop.io.gps.GPSListener;
import com.javislaptop.io.gps.impl.GpsDataRetriever;
import com.javislaptop.io.gps.model.PositionEvent;
import com.javislaptop.io.gps.model.VelocityEvent;
import com.javislaptop.telemetry.printer.Printer;
import com.javislaptop.telemetry.printer.PrinterType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class GpsService implements GPSListener {

    private final GpsDataRetriever gps;
    private final Printer printer;

    public GpsService(GpsDataRetriever gps, Printer printer) {
        this.gps = gps;
        this.printer = printer;
    }

    @PostConstruct
    public void init() {
        gps.addListener(this);
    }

    @PreDestroy
    public void shutdown() {
        gps.shutdown();
    }

    @Override
    public void onEvent(GPSEvent event) {
        if (event instanceof PositionEvent) {
            onEvent((PositionEvent) event);
        } else if (event instanceof VelocityEvent) {
            onEvent((VelocityEvent) event);
        }
    }

    private void onEvent(PositionEvent event) {
        final float latitude = event.getLocation().getLatitude();
        final float longitude = event.getLocation().getLongitude();
        printer.print(String.format("%s, %s", Printer.LAT_LNG_FORMAT.format(latitude), Printer.LAT_LNG_FORMAT.format(longitude)), PrinterType.POSITION);
    }

    private void onEvent(VelocityEvent event) {
        final float groundSpeed = event.getGroundSpeed();
        printer.print(Printer.DECIMAL_FORMAT.format(groundSpeed), PrinterType.SPEED);
    }
}
