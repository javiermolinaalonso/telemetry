package com.phoboss.io.gps.publisher;

import com.phoboss.io.gps.GPSEvent;
import org.springframework.stereotype.Component;

@Component
public class GpsSerialEventPublisher implements GpsEventPublisher {

    private final GpsSerialReader serial;

    public GpsSerialEventPublisher(GpsSerialReader serial) {
        this.serial = serial;
    }

    @Override
    public void publish() {
        GPSEvent event = serial.read();
    }
}
