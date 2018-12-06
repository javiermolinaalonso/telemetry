package com.javislaptop.io.gps.impl;

import com.javislaptop.io.gps.GPSEvent;
import com.javislaptop.io.gps.GpsDataProvider;
import com.javislaptop.io.gps.model.PositionEvent;
import com.javislaptop.io.gps.model.VelocityEvent;

import java.util.Arrays;
import java.util.List;

public class GpsDataProviderMock implements GpsDataProvider {

    @Override
    public List<GPSEvent> provide() {
        return Arrays.asList(new PositionEvent("$GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47"), new VelocityEvent("$GPVTG,054.7,T,034.4,M,005.5,N,010.2,K"));
    }

}
