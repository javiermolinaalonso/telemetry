package com.javislaptop.telemetry.service;

import com.javislaptop.io.accelerometer.Accelerometer;
import com.javislaptop.io.accelerometer.AccelerometerEvent;
import com.javislaptop.io.accelerometer.AccelerometerListener;
import com.javislaptop.io.accelerometer.Axis;
import com.javislaptop.telemetry.printer.PrinterConsole;
import com.javislaptop.telemetry.printer.PrinterType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static java.lang.Math.asin;
import static java.lang.Math.toDegrees;

@Service
public class LeanService implements AccelerometerListener {

    private final Accelerometer accelerometer;
    private final PrinterConsole printer;

    public LeanService(Accelerometer accelerometer, PrinterConsole printer) {
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
        final double gForce = Math.abs(event.getValue() - event.getZeroG()) / event.getgDelta();

        if (event.getAxis() == Axis.Z) {
            final double degrees = toDegrees(asin(gForce));
            printer.print(PrinterConsole.DECIMAL_FORMAT.format(degrees), PrinterType.LEAN);
        } else if (event.getAxis() == Axis.X) {
//            printer.print(PrinterConsole.DECIMAL_FORMAT.format(gForce), PrinterType.ACCELERATION);
        }
    }

}
