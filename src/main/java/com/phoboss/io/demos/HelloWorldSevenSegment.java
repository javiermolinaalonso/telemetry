package com.phoboss.io.demos;

import com.phoboss.io.sevensegment.SevenSegmentManager;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

public class HelloWorldSevenSegment {

    public static void main(String[] args) throws Exception {

        System.out.println("<--Pi4J--> GPIO Control Example ... started.");

        //22 is first digit
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        final SevenSegmentManager sevenSegmentManager = new SevenSegmentManager(gpio);

        for (int i = 0; i < 9999; i++) {
            sevenSegmentManager.print(i);
            Thread.sleep(20);
        }
        sevenSegmentManager.shutdown();
    }

}
