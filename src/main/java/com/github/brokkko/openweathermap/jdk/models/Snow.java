package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;

import java.util.Objects;

/**
 * Represents information about snowfall levels measured in millimeters.
 * <p>
 * A {@code Snow} instance may contain snowfall amounts for 1-hour
 * and 3-hour intervals. Each value must be non-negative; otherwise,
 * an {@link InvalidWeatherValueException} is thrown.
 */
public class Snow {
    private static final String DEFAULT_UNIT = "mm";

    private Double oneHourLevel;
    private Double threeHourLevel;

    private Snow() {
    }

    /**
     * Creates a {@link Snow} object containing only the 1-hour snowfall level.
     *
     * @param oneHourLevel snowfall level for the last hour, must be non-negative.
     * @return created {@link Snow} object.
     * @throws InvalidWeatherValueException if the provided value is negative.
     */
    public static Snow withOneHourLevelValue(double oneHourLevel) {
        final Snow snow = new Snow();
        snow.setOneHourLevel(oneHourLevel);
        return snow;
    }

    /**
     * Creates a {@link Snow} object containing only the 3-hour snowfall level.
     *
     * @param threeHourLevel snowfall level for the last 3 hours, must be non-negative.
     * @return created {@link Snow} object.
     * @throws InvalidWeatherValueException if the provided value is negative.
     */
    public static Snow withThreeHourLevelValue(double threeHourLevel) {
        final Snow snow = new Snow();
        snow.setThreeHourLevel(threeHourLevel);
        return snow;
    }

    /**
     * Creates a {@link Snow} object containing both the 1-hour and 3-hour snowfall levels.
     *
     * @param oneHourLevel   snowfall level for the last hour, must be non-negative.
     * @param threeHourLevel snowfall level for the last 3 hours, must be non-negative.
     * @return created {@link Snow} object.
     * @throws InvalidWeatherValueException if any provided value is negative.
     */
    public static Snow withValues(double oneHourLevel, double threeHourLevel) {
        final Snow snow = new Snow();
        snow.setOneHourLevel(oneHourLevel);
        snow.setThreeHourLevel(threeHourLevel);
        return snow;
    }

    /**
     * Returns snowfall level for the last hour.
     *
     * @return one-hour snowfall level or {@code null} if not set.
     */
    public Double getOneHourLevel() {
        return oneHourLevel;
    }

    /**
     * Sets snowfall level for the last hour.
     *
     * @param oneHourLevel snowfall level, must be non-negative.
     * @throws InvalidWeatherValueException if the value is negative.
     */
    public void setOneHourLevel(double oneHourLevel) {
        if (oneHourLevel < 0) {
            throw new InvalidWeatherValueException("Snow level value cannot be negative. Given: " + oneHourLevel);
        }
        this.oneHourLevel = oneHourLevel;
    }

    /**
     * Returns snowfall level for the last 3 hours.
     *
     * @return three-hour snowfall level or {@code null} if not set.
     */
    public Double getThreeHourLevel() {
        return threeHourLevel;
    }

    /**
     * Sets snowfall level for the last 3 hours.
     *
     * @param threeHourLevel snowfall level, must be non-negative.
     * @throws InvalidWeatherValueException if the value is negative.
     */
    public void setThreeHourLevel(double threeHourLevel) {
        if (threeHourLevel < 0) {
            throw new InvalidWeatherValueException("Snow level value cannot be negative. Given: " + threeHourLevel);
        }
        this.threeHourLevel = threeHourLevel;
    }

    /**
     * Returns the unit in which snowfall is measured ({@code mm}).
     *
     * @return snowfall unit.
     */
    public String getUnit() {
        return DEFAULT_UNIT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Snow snow)) return false;
        return Objects.equals(oneHourLevel, snow.oneHourLevel) &&
                Objects.equals(threeHourLevel, snow.threeHourLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oneHourLevel, threeHourLevel);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (oneHourLevel != null) {
            sb.append("1-hour snow level: ").append(oneHourLevel).append(getUnit());
        }
        if (threeHourLevel != null) {
            if (oneHourLevel != null) sb.append(", ");
            sb.append("3-hours snow level: ").append(threeHourLevel).append(getUnit());
        }
        return sb.toString();
    }
}

