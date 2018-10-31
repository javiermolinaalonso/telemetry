package com.phoboss;

import com.phoboss.accelerometer.AccelerometerManager;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

public class HelloWorldAccelerometer {

    public static void main(String[] args) throws Exception {

        System.out.println("<--Pi4J--> GPIO Control Example input analog ... started.");

        //22 is first digit
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        final AccelerometerManager accelerometerManager = new AccelerometerManager(gpio);

        System.out.println("Running program for 30 seconds");
        Thread.sleep(30000);

        System.exit(0);
    }
}
