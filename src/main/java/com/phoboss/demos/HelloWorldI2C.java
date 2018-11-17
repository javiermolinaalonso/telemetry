package com.phoboss.demos;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  I2CExample.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2018 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1115Pin;
import com.pi4j.gpio.extension.ads.ADS1x15GpioProvider;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.seven.segment.SevenSegmentManager;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.toDegrees;

/**
 * This example code demonstrates how to perform simple I2C
 * communication on the Raspberry Pi.  For this example we will
 * connect to a 'TSL2561' LUX sensor.
 * <p>
 * Data Sheet:
 * https://www.adafruit.com/datasheets/TSL256x.pdf
 * <p>
 * You should get something similar printed in the console
 * when executing this program:
 * <p>
 * > <--Pi4J--> I2C Example ... started.
 * > ... reading ID register from TSL2561
 * > TSL2561 ID = 0x50 (should be 0x50)
 * > ... powering up TSL2561
 * > ... reading DATA registers from TSL2561
 * > TSL2561 DATA 0 = 0x1e
 * > TSL2561 DATA 1 = 0x04
 * > ... powering down TSL2561
 * > Exiting bananapi.I2CExample
 *
 * @author Robert Savage
 */

public class HelloWorldI2C {

    public static final String AXIS_Y = "Axys Y";
    public static final String AXIS_Z = "Axis Z";
    private static final double MAX_VOLTAGE = 3.6;
    private static final double MIN_VOLTAGE = -0.3;
    private static final double ZERO_G_Z_VOLTAGE = 1.75;
    private static final double ZERO_G_VOLTAGE = 1.45;
    private static final double ONE_G_VOLTAGE = 1.75;
    private static final double G_DELTA = 0.30;
    private static final double MAX_G = 3;
    private static final double MIN_G = -3;

    public static void main(String args[]) throws InterruptedException, UnsupportedBusNumberException, IOException {

        System.out.println("<--Pi4J--> ADS1115 GPIO Example ... started.");

        // number formatters
        final DecimalFormat df = new DecimalFormat("#.##");
        final DecimalFormat pdf = new DecimalFormat("###.#");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // create custom ADS1115 GPIO provider
        final ADS1115GpioProvider gpioProvider = new ADS1115GpioProvider(I2CBus.BUS_1, ADS1115GpioProvider.ADS1115_ADDRESS_0x48);

        // provision gpio analog input pins from ADS1115
        GpioPinAnalogInput myInputs[] = {
//                gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A0, "Axis X"),
                gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A1, AXIS_Y),
                gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A2, AXIS_Z),
//                gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A3, "MyAnalogInput-A3"),
        };

        // ATTENTION !!
        // It is important to set the PGA (Programmable Gain Amplifier) for all analog input pins.
        // (You can optionally set each input to a different value)
        // You measured input voltage should never exceed this value!
        //
        // In my testing, I am using a Sharp IR Distance Sensor (GP2Y0A21YK0F) whose voltage never exceeds 3.3 VDC
        // (http://www.adafruit.com/products/164)
        //
        // PGA value PGA_4_096V is a 1:1 scaled input,
        // so the output values are in direct proportion to the detected voltage on the input pins
        gpioProvider.setProgrammableGainAmplifier(ADS1x15GpioProvider.ProgrammableGainAmplifierValue.PGA_4_096V, ADS1115Pin.ALL);


        // Define a threshold value for each pin for analog value change events to be raised.
        // It is important to set this threshold high enough so that you don't overwhelm your program with change events for insignificant changes
        gpioProvider.setEventThreshold(10, ADS1115Pin.ALL);


        // Define the monitoring thread refresh interval (in milliseconds).
        // This governs the rate at which the monitoring thread will read input values from the ADC chip
        // (a value less than 50 ms is not permitted)
        gpioProvider.setMonitorInterval(50);

        final SevenSegmentManager sevenSegmentManager = new SevenSegmentManager(gpio);

        GpioPinListenerAnalog listener = new GpioPinListenerAnalog() {
            @Override
            public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event) {
                // RAW value
                double value = event.getValue();

                // percentage
                double percent = ((value * 100) / ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE);
                double per_one = (value / ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE);

                // approximate voltage ( *scaled based on PGA setting )
                double voltage = gpioProvider.getProgrammableGainAmplifier(event.getPin()).getVoltage() * per_one;
                voltage = voltage * MAX_VOLTAGE / ADS1x15GpioProvider.ProgrammableGainAmplifierValue.PGA_4_096V.getVoltage();


                // display output
                if (event.getPin().getName().equals(AXIS_Z)) {
                    final double asinvalue = Math.abs(voltage - ZERO_G_Z_VOLTAGE) / G_DELTA;
                    final double degrees = toDegrees(acos(1-asinvalue));
                    System.out.println(" (" + event.getPin().getName() + ") : VOLTS=" + df.format(voltage) + "  | PERCENT=" + pdf.format(percent) + "% | RAW=" + value + "       ");
                    System.out.println(asinvalue + " " + degrees);
                    sevenSegmentManager.print(Double.valueOf(degrees).intValue());
                }
            }
        };

        myInputs[0].addListener(listener);
        myInputs[1].addListener(listener);
//        myInputs[2].addListener(listener);

        // keep program running for 10 minutes
        Thread.sleep(600000);

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();

        System.out.println("Exiting ADS1115GpioExample");
    }
}
