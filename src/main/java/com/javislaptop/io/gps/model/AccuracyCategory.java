package com.javislaptop.io.gps.model;

public enum AccuracyCategory {
    IDEAL(1, "Ideal",
            "This is the highest possible confidence level to be used for applications demanding the highest possible precision at all times."),
    EXCELLENT(
            2, "Excellent",
            "At this confidence level, positional measurements are considered accurate enough to meet all but the most sensitive applications."),
    GOOD(
            5,
            "Good",
            "Represents a level that marks the minimum appropriate for making business decisions. Positional measurements could be used to make reliable in-route navigation suggestions to the user."),
    MODERATE(
            10,
            "Moderate",
            "Positional measurements could be used for calculations, but the fix quality could still be improved. A more open view of the sky is recommended."),
    FAIR(
            20,
            "Fair",
            "Represents a low confidence level. Positional measurements should be discarded or used only to indicate a very rough estimate of the current location."),
    POOR(
            100,
            "Poor",
            "At this level, measurements are inaccurate by as much as 300 meters with a 6 meter accurate device (50 DOP × 6 meters) and should be discarded.");

    private final int dop;
    private String name;
    private String description;

    AccuracyCategory(int dop, String name, String description) {
        this.dop = dop;
        this.name = name;
        this.description = description;
    }

    /**
     * Looks up an Accuracy from a Dilution of Precision value.
     *
     * @param dop the dilution of precision measurement to get an Accuracy from.
     * @return the Accuracy corresponding to the dilution of precision.
     */
    public static AccuracyCategory fromDOP(float dop) {
        for (AccuracyCategory a : values()) {
            if (a.dop >= dop) {
                return a;
            }
        }
        return POOR;
    }

    /**
     * Returns the user friendly name of the accuracy.
     *
     * @return the user friendly name of the accuracy.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a longer description explaining the accuracy.
     *
     * @return a longer description explaining the accuracy.
     */
    public String getDescription() {
        return description;
    }

}
