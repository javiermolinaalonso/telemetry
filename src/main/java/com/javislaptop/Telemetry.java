package com.javislaptop;

import com.javislaptop.io.accelerometer.Accelerometer;
import com.javislaptop.io.accelerometer.AccelerometerAnalog;
import com.javislaptop.io.accelerometer.AccelerometerMock;
import com.javislaptop.io.gps.GpsDataProvider;
import com.javislaptop.io.gps.impl.GpsDataProviderMock;
import com.javislaptop.io.gps.impl.GpsDataProviderSerial;
import com.javislaptop.io.gps.impl.GpsParser;
import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1115Pin;
import com.pi4j.gpio.extension.ads.ADS1x15GpioProvider;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.serial.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = {"com.javislaptop.telemetry"})
@EnableScheduling
public class Telemetry {

    public static void main(final String[] args) {
        SpringApplication.run(Telemetry.class, args);
    }

    @Bean
    @Profile("raspi")
    public Accelerometer accelerometerRaspi() throws IOException, I2CFactory.UnsupportedBusNumberException {
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

        return new AccelerometerAnalog(myInputs, gpioProvider).init();
    }

    @Bean
    @Profile("mock")
    public Accelerometer accelerometerMock() {
        return new AccelerometerMock();
    }

    @Bean
    @Profile("raspi")
    public GpsDataProvider gpsRaspi() throws Exception {
        final Serial serial = SerialFactory.createInstance();
        SerialConfig config = new SerialConfig();
        config.device("/dev/ttyUSB0")
                .baud(Baud._9600)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);
        serial.open(config);

        return new GpsDataProviderSerial(serial, new GpsParser());
    }

    @Bean
    @Profile("mock")
    public GpsDataProvider gpsMock() {
        return new GpsDataProviderMock();
    }

}
