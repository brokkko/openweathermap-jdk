package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;

import java.util.Objects;

/**
 * The Clouds type represents cloudiness value percentage.
 * Its value can only be an integer in [0, 100] ranges.
 */
public class Clouds {
    private static final String DEFAULT_UNIT = "%";

    private byte value;

    /**
     * Instantiates a new Clouds.
     *
     * @param value the value representing cloudiness percentage.
     * @throws InvalidWeatherValueException in case if provided value isn't in allowed range.
     */
    private Clouds(byte value) {
        this.value = value;
    }

    /**
     * Static method for {@link Clouds} creation with value checking.
     * @param value clouds percentage value.
     * @return instantiated {@link Clouds} object.
     */
    public static Clouds withValue(byte value) {
        if (value < 0 || value > 100)  {
            throw new InvalidWeatherValueException(
                    "Cloudiness value must be in [0, 100] range. Given: " + value
            );
        }
        return new Clouds(value);
    }

    /**
     * Returns cloudiness percentage value.
     *
     * @return cloudiness percentage.
     */
    public byte getValue() {
        return value;
    }

    /**
     * Sets cloudiness percentage value.
     *
     * @param value new cloudiness value.
     * @throws InvalidWeatherValueException in case if provided value isn't in allowed range.
     */
    public void setValue(byte value) {
        if (value < 0 || value > 100) {
            throw new InvalidWeatherValueException(
                    "Cloudiness value must be in [0, 100] range. Given: " + value
            );
        }
        this.value = value;
    }

    /**
     * Returns cloudiness unitSystem. Constantly equals to '%'.
     *
     * @return the cloudiness unitSystem.
     */
    public String getUnit() {
        return DEFAULT_UNIT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Clouds clouds)) return false;
        return value == clouds.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Clouds: " + value + getUnit();
    }
}
