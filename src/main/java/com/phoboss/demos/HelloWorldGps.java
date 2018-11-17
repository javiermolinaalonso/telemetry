package com.phoboss.demos;

import com.gps.*;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.seven.segment.SevenSegmentManager;

public class HelloWorldGps {

    public static void main(String[] args) {
        final GPS gps = new GPS();
        System.out.println("GPS Initialized, waiting for events");

        final GpioController gpio = GpioFactory.getInstance();

        final SevenSegmentManager sevenSegmentManager = new SevenSegmentManager(gpio);

        gps.addListener(new GPSListener() {
            @Override
            public void onEvent(GPSEvent event) {
                if (event instanceof PositionEvent) {
                    onEvent((PositionEvent) event);
                } else if (event instanceof VelocityEvent) {
                    onEvent((VelocityEvent) event);
                }
            }

            void onEvent(PositionEvent event) {
                final float latitude = event.getLocation().getLatitude();
                final float longitude = event.getLocation().getLongitude();
                System.out.println(String.format("%s, %s", latitude, longitude));
                int lat = Float.valueOf(latitude * 100).intValue();
                sevenSegmentManager.print(lat);
            }

            void onEvent(VelocityEvent event) {
                final float groundSpeed = event.getGroundSpeed();
                System.out.println(groundSpeed);
            }
        });
    }

}
