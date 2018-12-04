package com.javislaptop.io.accelerometer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Thread.sleep;

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

    @Override
    public Accelerometer init() {
        new Thread(() -> {
            while (true) {
                this.listeners.forEach(listener -> listener.onEvent(generateRandomEvent()));
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return this;
    }

    private AccelerometerEvent generateRandomEvent() {
        return new AccelerometerEvent(Axis.Z, 0.3, 0, ThreadLocalRandom.current().nextDouble(-0.3, 0.3));
    }
}
