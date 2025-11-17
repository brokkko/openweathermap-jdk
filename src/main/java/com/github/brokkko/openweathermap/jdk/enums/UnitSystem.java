package com.github.brokkko.openweathermap.jdk.enums;

/**
 * An enumeration for supported unit systems with helper methods.
 */
public enum UnitSystem {
    /**
     * Metric units: Celsius, meter/sec, hPa, mm(rain, snow).
     */
    METRIC("metric"),

    /**
     * Imperial units: Fahrenheit, miles/hour, hPa, mm(rain, snow).
     */
    IMPERIAL("imperial"),

    /**
     * OpenWeatherMap standard units: Kelvin, meter/sec, hPa, mm(rain, snow).
     */
    STANDARD("standard");

    private final String value;

    UnitSystem(String value) {
        this.value = value;
    }

    /**
     * Returns wind unit for the current unit system.
     * @return wind unit.
     */
    public String getWindUnit() {
        return switch (this) {
            case IMPERIAL -> "miles/hour";
            default -> "meter/sec";
        };
    }

    /**
     * Returns temperature unit for the current unit system.
     * @return temperature unit.
     */
    public String getTemperatureUnit() {
        return switch (this) {
            case METRIC -> "°C";
            case IMPERIAL -> "°F";
            default -> "K";
        };
    }

    /**
     * Returns unit system value.
     * @return value unit system.
     */
    public String getValue() {
        return value;
    }
}
