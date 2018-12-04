package com.javislaptop.io.accelerometer;

public interface Accelerometer {

    void addListener(AccelerometerListener listener);

    Accelerometer init();

}
