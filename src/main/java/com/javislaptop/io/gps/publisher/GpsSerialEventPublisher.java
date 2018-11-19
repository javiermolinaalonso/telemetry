package com.javislaptop.io.gps.publisher;

import com.javislaptop.io.gps.GPSEvent;
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
