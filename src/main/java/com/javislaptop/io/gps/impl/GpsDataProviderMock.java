package com.javislaptop.io.gps.impl;

import com.javislaptop.io.gps.GPSEvent;
import com.javislaptop.io.gps.GpsDataProvider;
import com.javislaptop.io.gps.model.PositionEvent;

import java.util.Collections;
import java.util.List;

public class GpsDataProviderMock implements GpsDataProvider {

    @Override
    public List<GPSEvent> provide() {
        return Collections.singletonList(new PositionEvent("$GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47"));
    }

}
