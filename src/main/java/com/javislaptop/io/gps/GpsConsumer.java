package com.javislaptop.io.gps;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class GpsConsumer {

    private static final String POSITION_TAG = "$GPGGA";
    private static final String VELOCITY_TAG = "$GPVTG";

    public Optional<GPSEvent> consume(String dataLine) {
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
