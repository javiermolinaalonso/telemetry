package com.javislaptop.io.accelerometer;

import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;

import java.util.*;

import static java.util.stream.Stream.of;

public class AccelerometerAnalog implements GpioPinListenerAnalog, Accelerometer {

    //TODO this config could be configurable
    private static final double G_DELTA = 2640;

    private final GpioPinAnalogInput[] inputs;

    private List<AccelerometerListener> listeners;
    private final Map<Axis, List<Double>> calibrationValues;
    private final Map<Axis, Double> calibratedValues;

    public AccelerometerAnalog(GpioPinAnalogInput[] inputs) {
        this.inputs = inputs;
        this.listeners = new ArrayList<>();
        this.calibrationValues = new HashMap<>();
        this.calibratedValues = new HashMap<>();
    }

    @Override
    public Accelerometer init() {
        of(inputs).forEach(x -> x.addListener(this));
        return this;
    }

    @Override
    public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event) {
        getAxis(event).ifPresent(axis -> {
            final Optional<Double> calibratedValue = getCalibratedValue(axis);
            if (calibratedValue.isPresent()) {
                notifyListeners(new AccelerometerEvent(axis, G_DELTA, calibratedValue.get(), event.getValue()));
            } else {
                calibrate(axis, event.getValue());
            }
        });
    }

    private void calibrate(Axis axis, double value) {
        calibrationValues.putIfAbsent(axis, new ArrayList<>());
        calibrationValues.get(axis).add(value);
    }

    private void notifyListeners(AccelerometerEvent event) {
        listeners.forEach(listener -> listener.onEvent(event));
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

    private Optional<Double> getCalibratedValue(Axis axis) {
        final Double calibratedValue = calibratedValues.get(axis);
        if (calibratedValue == null) {
            final boolean calibrated = calibrationValues.getOrDefault(axis, new ArrayList<>()).size() >= 10;
            if (calibrated) {
                calibratedValues.put(axis, calibrationValues.get(axis).stream().mapToDouble(x -> x).average().getAsDouble());
            }
            return Optional.empty();
        } else {
            return Optional.of(calibratedValue);
        }
    }

}
