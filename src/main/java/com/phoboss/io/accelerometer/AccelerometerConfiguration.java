package com.phoboss.io.accelerometer;

import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1115Pin;
import com.pi4j.gpio.extension.ads.ADS1x15GpioProvider;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class AccelerometerConfiguration {

    @Bean
    public Accelerometer accelerometer() throws IOException, I2CFactory.UnsupportedBusNumberException {
        final GpioController gpioController = GpioFactory.getInstance();
        final ADS1115GpioProvider gpioProvider = new ADS1115GpioProvider(I2CBus.BUS_1, ADS1115GpioProvider.ADS1115_ADDRESS_0x48);
        gpioProvider.setProgrammableGainAmplifier(ADS1x15GpioProvider.ProgrammableGainAmplifierValue.PGA_4_096V, ADS1115Pin.ALL);
        gpioProvider.setEventThreshold(10, ADS1115Pin.ALL);
        gpioProvider.setMonitorInterval(50);

        GpioPinAnalogInput myInputs[] = {
                gpioController.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A0, "Axis X"),
                gpioController.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A1, "Axis Y"),
                gpioController.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A2, "Axis Z"),
                gpioController.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A3, "UNK"),
        };

        return new Accelerometer(myInputs, gpioProvider).init();
    }
}
