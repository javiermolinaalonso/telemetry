package com.phoboss.accelerometer;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.seven.segment.SevenSegmentManager;

public class AccelerometerManager {

    private final GpioPinDigitalInput[] acceleration;

    public AccelerometerManager(GpioController gpio) {
        acceleration = new GpioPinDigitalInput[]{
                gpio.provisionDigitalInputPin(RaspiPin.GPIO_08)
        };

        for (GpioPinDigitalInput gpioPinDigitalInput : acceleration) {
            gpioPinDigitalInput.addListener((GpioPinListenerDigital) event -> System.out.println(String.format("Received %s at pin %s", event.getEdge().getValue(), event.getPin().getPin().getName())));
        }
    }
}
