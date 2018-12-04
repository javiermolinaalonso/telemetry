package com.javislaptop.telemetry.service;

import com.javislaptop.io.accelerometer.Accelerometer;
import com.javislaptop.io.accelerometer.AccelerometerEvent;
import com.javislaptop.io.accelerometer.AccelerometerListener;
import com.javislaptop.io.accelerometer.Axis;
import com.javislaptop.telemetry.printer.Printer;
import com.javislaptop.telemetry.printer.PrinterType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static java.lang.Math.acos;
import static java.lang.Math.toDegrees;

@Service
public class LeanService implements AccelerometerListener {

    private final Accelerometer accelerometer;
    private final Printer printer;

    public LeanService(Accelerometer accelerometer, Printer printer) {
        this.accelerometer = accelerometer;
        this.printer = printer;
    }

    @PostConstruct
    public void init() {
        this.accelerometer.addListener(this);
        this.accelerometer.init();
    }

    @Override
    public void onEvent(AccelerometerEvent event) {
        if (event.getAxis() == Axis.Z) {
            final double asinvalue = Math.abs(event.getValue() - event.getZeroG()) / event.getgDelta();
            final double degrees = toDegrees(acos(1 - asinvalue));
            printer.print(Printer.DECIMAL_FORMAT.format(degrees), PrinterType.LEAN);
        }
    }

}
