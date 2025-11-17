package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;

import java.util.Objects;

/**
 * Represents the relative humidity value expressed as a percentage.
 * The allowed range is from 0 to 100 inclusive.
 */
public class Humidity {
    private static final String DEFAULT_UNIT = "%";

    private int value;

    /**
     * Private constructor to enforce value validation via factory method.
     *
     * @param value humidity percentage.
     */
    private Humidity(int value) {
        this.value = value;
    }

    /**
     * Creates a {@link Humidity} instance after validating the provided value.
     *
     * @param value humidity percentage.
     * @return a valid {@link Humidity} object.
     * @throws InvalidWeatherValueException if the value is outside the [0, 100] range.
     */
    public static Humidity withValue(int value) {
        if (value < 0 || value > 100) {
            throw new InvalidWeatherValueException(
                    "Humidity value must be in [0, 100] range. Given: " + value
            );
        }
        return new Humidity(value);
    }

    /**
     * Returns the humidity percentage value.
     *
     * @return humidity percentage.
     */
    public int getValue() {
        return value;
    }

    /**
     * Updates the humidity value after validating it.
     *
     * @param value new humidity value.
     * @throws InvalidWeatherValueException if the value is outside the [0, 100] range.
     */
    public void setValue(int value) {
        if (value < 0 || value > 100) {
            throw new InvalidWeatherValueException(
                    "Humidity value must be in [0, 100] range. Given: " + value
            );
        }
        this.value = value;
    }

    /**
     * Returns the unit of measure. Always equal to "%".
     *
     * @return unit string.
     */
    public String getUnit() {
        return DEFAULT_UNIT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Humidity humidity)) return false;
        return value == humidity.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Humidity: " + value + getUnit();
    }
}
