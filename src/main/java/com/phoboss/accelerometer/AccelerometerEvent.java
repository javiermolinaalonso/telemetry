package com.phoboss.accelerometer;

public class AccelerometerEvent {

    private final Axis axis;
    private final double gDelta;
    private final double zeroG;
    private final double value;

    public AccelerometerEvent(Axis axis, double gDelta, double zeroG, double value) {
        this.axis = axis;
        this.gDelta = gDelta;
        this.zeroG = zeroG;
        this.value = value;
    }

    public Axis getAxis() {
        return axis;
    }

    public double getValue() {
        return value;
    }

    public double getgDelta() {
        return gDelta;
    }

    public double getZeroG() {
        return zeroG;
    }
}
