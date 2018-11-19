package com.javislaptop.io.gps;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GpsConfiguration {
    @Bean
    public GPS gps() {
        return new GPS();
    }
}
