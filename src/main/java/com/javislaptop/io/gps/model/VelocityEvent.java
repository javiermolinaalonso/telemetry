package com.javislaptop.io.gps.model;

import com.javislaptop.io.gps.GPSEvent;

public class VelocityEvent implements GPSEvent {
    private float trueTrackMadeGood = Float.NaN;
    private float magneticTrackMadeGood = Float.NaN;
    private float groundSpeed = Float.NaN;

    public VelocityEvent(String data) {
        parse(data);
    }

    /**
     * Returns the measured heading in degrees.
     *
     * @return the measured heading in degrees.
     */
    public float getTrueTrackMadeGood() {
        return trueTrackMadeGood;
    }

    /**
     * Returns the measured magnetic heading. According to the chip manual this
     * seems to "need GlobalTop Customization Service".
     *
     * @return the measured magnetic heading.
     */
    public float getMagneticTrackMadeGood() {
        return magneticTrackMadeGood;
    }

    /**
     * Returns the horizontal speed in km/h.
     *
     * @return the horizontal speed in km/h.
     */
    public float getGroundSpeed() {
        return groundSpeed;
    }

    @Override
    public String toString() {
        return String.format("True: %.1f\u00B0 Magnetic: %.1f\u00B0 Speed: %.1f km/h", getTrueTrackMadeGood(), getMagneticTrackMadeGood(),
                getGroundSpeed());
    }

    protected void parse(String data) {
        String[] args = data.split(",");
        if (args.length >= 8) {
            trueTrackMadeGood = getFloat(args[1]);
            magneticTrackMadeGood = getFloat(args[3]);
            groundSpeed = getFloat(args[7]);
        }
    }

    private float getFloat(String string) {
        if (string == null || "".equals(string)) {
            return Float.NaN;
        }
        return Float.parseFloat(string);
    }
}