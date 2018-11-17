package com;

import com.phoboss.accelerometer.AccelerometerEvent;
import com.phoboss.accelerometer.AccelerometerListener;
import com.phoboss.accelerometer.Axis;
import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1x15GpioProvider;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.acos;
import static java.lang.Math.toDegrees;
import static java.util.stream.Stream.of;

public class Accelerometer implements GpioPinListenerAnalog {

    private static final double MAX_VOLTAGE = 3.6;
    private static final double G_DELTA = 0.30;
    private static final double ZERO_G_Z_VOLTAGE = 1.75;

    private final GpioPinAnalogInput[] inputs;
    private final ADS1115GpioProvider gpioProvider;

    private List<AccelerometerListener> listeners;

    public Accelerometer(GpioPinAnalogInput[] inputs, ADS1115GpioProvider gpioProvider) {
        this.inputs = inputs;
        this.gpioProvider = gpioProvider;
        this.listeners = new ArrayList<>();
    }

    public Accelerometer init() {
        of(inputs).forEach(x -> x.addListener(this));
        return this;
    }

    @Override
    public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event) {
        // RAW value
        double value = event.getValue();

        // percentage
        double percent = ((value * 100) / ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE);
        double per_one = (value / ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE);

        // approximate voltage ( *scaled based on PGA setting )
        double voltage = gpioProvider.getProgrammableGainAmplifier(event.getPin()).getVoltage() * per_one;
        final double scaledVoltage = voltage * MAX_VOLTAGE / ADS1x15GpioProvider.ProgrammableGainAmplifierValue.PGA_4_096V.getVoltage();


        // display output
        if (event.getPin().getName().equals("Axis Z")) {
            listeners.forEach(x -> x.onEvent(new AccelerometerEvent(Axis.Z, G_DELTA, ZERO_G_Z_VOLTAGE, scaledVoltage)));
        }
    }

    public void addListener(AccelerometerListener listener) {
        this.listeners.add(listener);
    }
}
