package com.phoboss.telemetry.service;

import com.phoboss.io.accelerometer.Accelerometer;
import com.phoboss.io.accelerometer.AccelerometerEvent;
import com.phoboss.io.accelerometer.AccelerometerListener;
import com.phoboss.io.accelerometer.Axis;
import com.phoboss.telemetry.printer.Printer;
import com.phoboss.telemetry.printer.PrinterType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static com.phoboss.telemetry.printer.Printer.DECIMAL_FORMAT;
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
    }

    @Override
    public void onEvent(AccelerometerEvent event) {
        if (event.getAxis() == Axis.Z) {
            final double asinvalue = Math.abs(event.getValue() - event.getZeroG()) / event.getgDelta();
            final double degrees = toDegrees(acos(1 - asinvalue));
            printer.print(DECIMAL_FORMAT.format(degrees), PrinterType.LEAN);
        }
    }

}
