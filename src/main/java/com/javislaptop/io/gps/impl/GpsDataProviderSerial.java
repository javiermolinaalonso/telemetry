package com.javislaptop.io.gps.impl;


import com.javislaptop.io.gps.GPSEvent;
import com.javislaptop.io.gps.GpsDataProvider;
import com.pi4j.io.serial.Serial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class GpsDataProviderSerial implements GpsDataProvider {

    private static final Logger logger = LoggerFactory.getLogger(GpsDataProviderSerial.class);

    private final Serial serial;
    private final GpsParser gpsParser;

    public GpsDataProviderSerial(Serial serial, GpsParser gpsParser) {
        this.serial = serial;
        this.gpsParser = gpsParser;
    }

    @Override
    public List<GPSEvent> provide() {
        try {
            int available = serial.available();
            if (available > 0) {
                return gpsParser.parse(new String(serial.read(available)));
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            logger.error("Exception while reading serial", e);
            return Collections.emptyList();
        }
    }

}
