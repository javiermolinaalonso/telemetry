package com.javislaptop.io.demos;

import com.javislaptop.io.gps.GPSEvent;
import com.javislaptop.io.gps.GPSListener;
import com.javislaptop.io.gps.impl.GpsDataProviderSerial;
import com.javislaptop.io.gps.impl.GpsDataRetriever;
import com.javislaptop.io.gps.model.PositionEvent;
import com.javislaptop.io.gps.model.VelocityEvent;
import com.javislaptop.io.sevensegment.SevenSegmentManager;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.serial.*;

public class HelloWorldGps {

    public static void main(String[] args) throws Exception {
        final Serial serial = SerialFactory.createInstance();
        SerialConfig config = new SerialConfig();
        config.device("/dev/ttyUSB0")
                .baud(Baud._9600)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);
        serial.open(config);

        final GpsDataRetriever gps = new GpsDataRetriever(new GpsDataProviderSerial(serial));
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
