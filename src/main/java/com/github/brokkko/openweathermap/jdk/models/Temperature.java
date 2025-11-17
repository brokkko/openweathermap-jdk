package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;

import java.util.Objects;

/**
 * Represents a temperature measurement along with optional metadata such as
 * maximal, minimal, and "feels like" temperature values. A temperature is expressed
 * as a numeric value together with its unit (e.g., "°C", "°F", "K").
 *
 * <p>Instances of this class are created through the {@link #withValue(double, String)}
 * factory method, which ensures that the unit is always valid (non-null).
 * Additional temperature-related values can be assigned through setter methods.
 */
public class Temperature {

    /**
     * The primary temperature value.
     */
    private double value;

    /**
     * The maximal observed temperature, if known.
     */
    private Double maxTemperature;

    /**
     * The minimal observed temperature, if known.
     */
    private Double minTemperature;

    /**
     * The perceived temperature ("feels like"), if available.
     */
    private Double feelsLike;

    /**
     * The temperature unit (e.g., "°C", "°F", "K"). Cannot be null.
     */
    private String unit;

    /**
     * Constructs a new {@code Temperature} instance.
     * Should be created only through {@link #withValue(double, String)}.
     *
     * @param value the temperature value
     * @param unit  the temperature unit; must not be null
     */
    private Temperature(double value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    /**
     * Creates a new {@link Temperature} instance with correctness check.
     *
     * @param value the temperature value
     * @param unit  the temperature unit (must not be null)
     * @return a new {@link Temperature} instance
     * @throws InvalidWeatherValueException if unit is null
     */
    public static Temperature withValue(double value, String unit) {
        if (unit == null) {
            throw new InvalidWeatherValueException("Unit must be set.");
        }
        return new Temperature(value, unit);
    }

    /**
     * Returns the temperature value.
     * @return the temperature value
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets temperature value.
     * @param value temperature
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Returns maximal temperature value.
     * @return maximal temperature value
     */
    public Double getMaxTemperature() {
        return maxTemperature;
    }

    /**
     * Sets maximal temperature value.
     * @param maxTemperature maximal temperature
     */
    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    /**
     * Returns minimal temperature value.
     * @return minimal temperature value
     */
    public Double getMinTemperature() {
        return minTemperature;
    }

    /**
     * Sets minimal temperature value.
     * @param minTemperature minimal temperature
     */
    public void setMinTemperature(Double minTemperature) {
        this.minTemperature = minTemperature;
    }

    /**
     * Returns 'feels like' temperature value.
     * @return 'feels like' temperature value
     */
    public Double getFeelsLike() {
        return feelsLike;
    }

    /**
     * Sets 'feels like' temperature value.
     * @param feelsLike 'feels like' temperature
     */
    public void setFeelsLike(Double feelsLike) {
        this.feelsLike = feelsLike;
    }

    /**
     * Returns temperature unit.
     * @return unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets temperature unit with correctness check.
     * @param unit temperature unit
     */
    public void setUnit(String unit) {
        if (unit == null) {
            throw new InvalidWeatherValueException("Unit must be set.");
        }
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Temperature that)) return false;
        return Double.compare(that.value, value) == 0 &&
                Objects.equals(maxTemperature, that.maxTemperature) &&
                Objects.equals(minTemperature, that.minTemperature) &&
                Objects.equals(feelsLike, that.feelsLike) &&
                Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, maxTemperature, minTemperature, feelsLike, unit);
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Temperature: ");
        stringBuilder.append(value);
        stringBuilder.append(' ');
        stringBuilder.append(unit);
        if (maxTemperature != null) {
            stringBuilder.append(", Maximum value: ");
            stringBuilder.append(maxTemperature);
            stringBuilder.append(' ');
            stringBuilder.append(unit);
        }
        if (minTemperature != null) {
            stringBuilder.append(", Minimum value: ");
            stringBuilder.append(minTemperature);
            stringBuilder.append(' ');
            stringBuilder.append(unit);
        }
        if (feelsLike != null) {
            stringBuilder.append(", Feels like: ");
            stringBuilder.append(feelsLike);
            stringBuilder.append(' ');
            stringBuilder.append(unit);
        }

        return stringBuilder.toString();
    }
}
