package com.gps.publisher;

import com.gps.GPSEvent;
import com.pi4j.io.serial.Serial;
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
