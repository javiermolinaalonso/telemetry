package com.javislaptop.io.gps;

import java.util.List;

public interface GpsDataProvider {

    List<GPSEvent> provide();

    void shutdown();
}
