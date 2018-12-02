package com.javislaptop.io.gps;

import java.util.Optional;

public interface GpsDataProvider {

    Optional<String> provide();

    void shutdown();
}
