package com.javislaptop.io.demos;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

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
