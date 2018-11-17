package com.phoboss.telemetry;

import com.gps.*;
import com.phoboss.telemetry.printer.Printer;
import com.phoboss.telemetry.printer.PrinterType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static com.phoboss.telemetry.printer.Printer.DECIMAL_FORMAT;
import static com.phoboss.telemetry.printer.Printer.LAT_LNG_FORMAT;

@Service
public class GpsService implements GPSListener {

    private final GPS gps;
    private final Printer printer;

    public GpsService(GPS gps, Printer printer) {
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
        printer.print(String.format("%s, %s", LAT_LNG_FORMAT.format(latitude), LAT_LNG_FORMAT.format(longitude)), PrinterType.POSITION);
    }

    private void onEvent(VelocityEvent event) {
        final float groundSpeed = event.getGroundSpeed();
        printer.print(DECIMAL_FORMAT.format(groundSpeed), PrinterType.SPEED);
    }
}
