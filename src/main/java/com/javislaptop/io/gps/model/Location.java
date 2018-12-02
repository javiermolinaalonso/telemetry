package com.javislaptop.io.gps.model;

public class Location {
    private final static String STR_DEGREE = "\u00B0";
    private final static String STR_MINUTE = "'"; // "\u2032";
    private final static String STR_SECOND = "\""; // \u2033";

    private final float latitude;
    private final float longitude;

    /**
     * Creates a new location.
     *
     * @param latitude
     *            the latitude in decimal degrees.
     * @param longitude
     *            the longitude in decimal degrees.
     */
    public Location(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return the decimal degree for the latitude.
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * @return the decimal degree for the longitude.
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * Returns the coordinates as a String in degrees, minutes and seconds format.
     *
     * @return the coordinates as a String in degrees, minutes and seconds format.
     */
    public String asDMS() {
        return String.format("%s%s %s%s", toDMS(latitude), latitude > 0 ? "N" : "S", toDMS(longitude), longitude > 0 ? "E" : "W");
    }

    private Object toDMS(float coordinate) {
        int deg = Math.abs((int) coordinate);
        int minute = Math.abs((int) (coordinate * 60) % 60);
        int second = Math.abs((int) (coordinate * 3600) % 60);
        return String.format("%d%s%d%s%d%s", deg, STR_DEGREE, minute, STR_MINUTE, second, STR_SECOND);
    }

    @Override
    public String toString() {
        return asDMS();
    }
}
