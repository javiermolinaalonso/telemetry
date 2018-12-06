package com.javislaptop.io.accelerometer;

import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1x15GpioProvider;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Stream.of;

public class AccelerometerAnalog implements GpioPinListenerAnalog, Accelerometer {

    //TODO this config could be configurable
    private static final double MAX_VOLTAGE = 3.6;
    private static final double G_DELTA = 0.30;
    private static final double ZERO_G_Z_VOLTAGE = 1.75;

    private final GpioPinAnalogInput[] inputs;
    private final ADS1115GpioProvider gpioProvider;

    private List<AccelerometerListener> listeners;
    private final List<Double> calibrationValues;

    public AccelerometerAnalog(GpioPinAnalogInput[] inputs, ADS1115GpioProvider gpioProvider) {
        this.inputs = inputs;
        this.gpioProvider = gpioProvider;
        this.listeners = new ArrayList<>();
    }

    @Override
    public Accelerometer init() {
        of(inputs).forEach(x -> x.addListener(this));
        return this;
    }

    @Override
    public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event) {
        if (isCalibrated()) {
            notifyListeners(event);
        } else {
            calibrate();
        }
    }

    private void calibrate() {

    }

    private void notifyListeners(GpioPinAnalogValueChangeEvent event) {
        getAxis(event).ifPresent(axis -> listeners.forEach(listener -> {
            final AccelerometerEvent accelerometerEvent = new AccelerometerEvent(axis, G_DELTA, ZERO_G_Z_VOLTAGE, getScaledVoltage(event));
            listener.onEvent(accelerometerEvent);
        }));
    }

    private double getScaledVoltage(GpioPinAnalogValueChangeEvent event) {
        double per_one = (event.getValue() / ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE);

        // approximate voltage ( *scaled based on PGA setting )
        double voltage = gpioProvider.getProgrammableGainAmplifier(event.getPin()).getVoltage() * per_one;
        return voltage * MAX_VOLTAGE / ADS1x15GpioProvider.ProgrammableGainAmplifierValue.PGA_4_096V.getVoltage();
    }

    private Optional<Axis> getAxis(GpioPinAnalogValueChangeEvent event) {
        if (event.getPin().getName().equals("Axis Z")) {
            return Optional.of(Axis.Z);
        } else if (event.getPin().getName().equals("Axis Y")) {
            return Optional.of(Axis.Y);
        } else if (event.getPin().getName().equals("Axis X")) {
            return Optional.of(Axis.X);
        }
        return Optional.empty();
    }

    public void addListener(AccelerometerListener listener) {
        this.listeners.add(listener);
    }

    private boolean isCalibrated() {
        return calibrated;
    }

}
