package com.javislaptop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class TelemetryMock {

    public static void main(final String[] args) {
        SpringApplication.run(TelemetryMock.class, args);
    }

}
