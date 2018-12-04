package com.javislaptop.io.gps.impl;

import com.javislaptop.io.gps.GPSEvent;
import com.javislaptop.io.gps.model.PositionEvent;
import com.javislaptop.io.gps.model.VelocityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class GpsParser {

    private static final String POSITION_TAG = "$GPGGA";
    private static final String VELOCITY_TAG = "$GPVTG";

    private StringBuilder builder;

    public GpsParser() {
        this.builder = new StringBuilder();
    }

    synchronized List<GPSEvent> parse(String str) {
        StringTokenizer st = new StringTokenizer(str, "\n", true);
        List<GPSEvent> events = new ArrayList<>();
        while (st.hasMoreElements()) {
            String dataLine = st.nextToken();
            while ("\n".equals(dataLine) && st.hasMoreElements()) {
                dataLine = st.nextToken();
            }
            if (st.hasMoreElements()) {
                consume(dataLine).ifPresent(events::add);
            } else if (!"\n".equals(dataLine)) {
                builder.append(dataLine);
            }
        }
        return events;
    }

    private Optional<GPSEvent> consume(String dataLine) {
        if (hasValidCheckSum(dataLine)) {
            if (dataLine.startsWith(POSITION_TAG)) {
                return of(new PositionEvent(dataLine));
            } else if (dataLine.startsWith(VELOCITY_TAG)) {
                return of(new VelocityEvent(dataLine));
            }
        }
        return empty();
    }

    private boolean hasValidCheckSum(String data) {
        if (!data.startsWith("$")) {
            return false;
        }
        int indexOfStar = data.indexOf('*');
        if (indexOfStar <= 0 || indexOfStar >= data.length()) {
            return false;
        }
        String chk = data.substring(1, indexOfStar);
        String checksumStr = data.substring(indexOfStar + 1);
        int valid = Integer.parseInt(checksumStr.trim(), 16);
        int checksum = 0;
        for (int i = 0; i < chk.length(); i++) {
            checksum = checksum ^ chk.charAt(i);
        }
        return checksum == valid;
    }
}
