package com.javislaptop.io.accelerometer;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AccelerometerMock implements Accelerometer {

    private List<AccelerometerListener> listeners;

    public AccelerometerMock() {
        this(new ArrayList<>());
    }

    private AccelerometerMock(List<AccelerometerListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void addListener(AccelerometerListener listener) {
        this.listeners.add(listener);
    }

    @Scheduled(fixedRate = 100)
    void notifyListeners() {
        this.listeners.forEach(listener -> listener.onEvent(generateRandomEvent()));
    }

    @Override
    public Accelerometer init() {
        return this;
    }

    private AccelerometerEvent generateRandomEvent() {
        return new AccelerometerEvent(Axis.Z, 0.3, 0, ThreadLocalRandom.current().nextDouble(-0.2, 0.2));
    }
}
