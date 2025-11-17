package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;

import java.util.Objects;

/**
 * Represents rain information in millimeters for 1-hour and 3-hour intervals.
 */
public class Rain {
    private static final String DEFAULT_UNIT = "mm";

    private Double oneHourLevel;
    private Double threeHourLevel;

    private Rain() {
    }

    /**
     * Creates {@link Rain} object with correctness check.
     *
     * @param oneHourLevel 1-hour rain level value
     * @return rain object.
     */
    public static Rain withOneHourLevelValue(double oneHourLevel) {
        final Rain rain = new Rain();
        rain.setOneHourLevel(oneHourLevel);
        return rain;
    }

    /**
     * Creates {@link Rain} object with correctness check.
     *
     * @param threeHourLevel 3-hour rain level value
     * @return rain object.
     */
    public static Rain withThreeHourLevelValue(double threeHourLevel) {
        final Rain rain = new Rain();
        rain.setThreeHourLevel(threeHourLevel);
        return rain;
    }

    /**
     * Creates {@link Rain} object with correctness check.
     *
     * @param oneHourLevel   the one hour rain level
     * @param threeHourLevel the three hour rain level
     * @return the rain
     */
    public static Rain withValues(double oneHourLevel, double threeHourLevel) {
        final Rain rain = new Rain();
        rain.setOneHourLevel(oneHourLevel);
        rain.setThreeHourLevel(threeHourLevel);
        return rain;
    }

    /**
     * Gets one-hour rain level.
     *
     * @return the one-hour rain level
     */
    public Double getOneHourLevel() {
        return oneHourLevel;
    }

    /**
     * Sets one-hour rain level.
     *
     * @param oneHourLevel the one-hour rain level
     */
    public void setOneHourLevel(double oneHourLevel) {
        if (oneHourLevel < 0) {
            throw new InvalidWeatherValueException("Rain level value cannot be negative. Given: " + oneHourLevel);
        }
        this.oneHourLevel = oneHourLevel;
    }

    /**
     * Gets three-hour rain level.
     *
     * @return the three-hour rain level
     */
    public Double getThreeHourLevel() {
        return threeHourLevel;
    }

    /**
     * Sets three-hour rain level.
     *
     * @param threeHourLevel the three-hour rain level
     */
    public void setThreeHourLevel(double threeHourLevel) {
        if (threeHourLevel < 0) {
            throw new InvalidWeatherValueException("Rain level value cannot be negative. Given: " + oneHourLevel);
        }
        this.threeHourLevel = threeHourLevel;
    }

    /**
     * Gets unit.
     *
     * @return the unit
     */
    public String getUnit() {
        return DEFAULT_UNIT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rain rain)) return false;
        return Objects.equals(oneHourLevel, rain.oneHourLevel) &&
                Objects.equals(threeHourLevel, rain.threeHourLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oneHourLevel, threeHourLevel);
    }

    @Override
    public String toString() {
        final StringBuilder snowString = new StringBuilder();
        if (oneHourLevel != null) {
            snowString.append("1-hour rain level: ");
            snowString.append(oneHourLevel);
            snowString.append(getUnit());
        }
        if (threeHourLevel != null) {
            if (oneHourLevel != null) {
                snowString.append(", ");
            }
            snowString.append("3-hours rain level: ");
            snowString.append(threeHourLevel);
            snowString.append(getUnit());
        }
        return snowString.toString();
    }
}

