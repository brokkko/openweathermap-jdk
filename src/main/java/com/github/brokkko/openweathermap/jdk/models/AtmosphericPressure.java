package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;

import java.util.Objects;

/**
 * Represents atmospheric pressure with optional sea-level and ground-level components.
 * <p>
 * The pressure value must always be non-negative. Any attempt to assign a negative value
 * results in {@link InvalidWeatherValueException}.
 */
public class AtmosphericPressure {
    private static final String DEFAULT_UNIT = "hPa";

    private double value;

    private Double seaLevelValue;
    private Double groundLevelValue;

    /**
     * Creates a new AtmosphericPressure instance.
     *
     * @param value the atmospheric pressure value (non-negative).
     * @throws InvalidWeatherValueException if the provided value is negative.
     */
    private AtmosphericPressure(double value) {
        this.value = value;
    }

    /**
     * Factory method for creating an {@link AtmosphericPressure} instance with validation.
     *
     * @param value the pressure value to set (must be >= 0).
     * @return a new {@link AtmosphericPressure} instance.
     * @throws InvalidWeatherValueException if the provided value is negative.
     */
    public static AtmosphericPressure withValue(double value) {
        if (value < 0)  {
            throw new InvalidWeatherValueException(
                    "Atmospheric pressure value must be non-negative. Given: " + value
            );
        }
        return new AtmosphericPressure(value);
    }

    /**
     * Returns pressure value.
     *
     * @return pressure value.
     */
    public double getValue() {
        return value;
    }

    /**
     * Updates the pressure value.
     *
     * @param value the new pressure value (must be >= 0).
     * @throws InvalidWeatherValueException if the provided value is negative.
     */
    public void setValue(double value) {
        if (value < 0)  {
            throw new InvalidWeatherValueException(
                    "Atmospheric pressure value must be non-negative. Given: " + value
            );
        }
        this.value = value;
    }

    /**
     * Gets sea level value.
     *
     * @return the sea level value.
     */
    public Double getSeaLevelValue() {
        return seaLevelValue;
    }

    /**
     * Sets sea level value if present.
     *
     * @param seaLevelValue the sea level value.
     * @throws InvalidWeatherValueException in case if provided value isn't in an allowed range.
     */
    public void setSeaLevelValue(double seaLevelValue) {
        if (seaLevelValue < 0)  {
            throw new InvalidWeatherValueException(
                    "Atmospheric pressure value must be non-negative. Given: " + value
            );
        }
        this.seaLevelValue = seaLevelValue;
    }

    /**
     * Gets ground level value.
     *
     * @return the ground level value.
     */
    public Double getGroundLevelValue() {
        return groundLevelValue;
    }

    /**
     * Sets the ground-level pressure value.
     *
     * @param groundLevelValue the new value (must be >= 0).
     * @throws InvalidWeatherValueException if the provided value is negative.
     */
    public void setGroundLevelValue(double groundLevelValue) {
        if (groundLevelValue < 0)  {
            throw new InvalidWeatherValueException(
                    "Atmospheric pressure value must be non-negative. Given: " + value
            );
        }
        this.groundLevelValue = groundLevelValue;
    }

    /**
     * Returns the measurement unit ("hPa").
     *
     * @return the unit name.
     */
    public String getUnit() {
        return DEFAULT_UNIT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AtmosphericPressure atmosphericPressure)) return false;
        return Double.compare(atmosphericPressure.value, value) == 0 &&
                Objects.equals(seaLevelValue, atmosphericPressure.seaLevelValue) &&
                Objects.equals(groundLevelValue, atmosphericPressure.groundLevelValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, seaLevelValue, groundLevelValue);
    }

    @Override
    public String toString() {
        return "Pressure: " + value + ' ' + getUnit();
    }
}
