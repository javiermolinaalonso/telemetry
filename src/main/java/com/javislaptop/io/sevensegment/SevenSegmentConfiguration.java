package com.javislaptop.io.sevensegment;

import com.pi4j.io.gpio.GpioFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SevenSegmentConfiguration {

    @Bean
    public SevenSegmentManager sevenSegmentManager() {
        return new SevenSegmentManager(GpioFactory.getInstance());
    }

}
