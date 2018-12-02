package com.javislaptop.io.gps.impl;


import com.javislaptop.io.gps.GpsDataProvider;
import com.pi4j.io.serial.Serial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class GpsDataProviderSerial implements GpsDataProvider {

    private static final Logger logger = LoggerFactory.getLogger(GpsDataProviderSerial.class);

    private final Serial serial;

    public GpsDataProviderSerial(Serial serial) {
        this.serial = serial;
    }

    @Override
    public Optional<String> provide() {
        try {
            int available = serial.available();
            if (available > 0) {
                return Optional.of(new String(serial.read(available)));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Exception while reading serial", e);
            return Optional.empty();
        }
    }

    @Override
    public void shutdown() {
        try {
            serial.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
