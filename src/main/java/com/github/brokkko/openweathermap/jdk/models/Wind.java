package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;

import java.util.Objects;

/**
 * The type Wind.
 */
public class Wind {
    private double speed;
    private Double degrees;
    private Double gust;
    private String unit;

    /**
     * Instantiates a new Wind.
     *
     * @param speed the speed
     * @param unit  the unitSystem
     */
    private Wind(double speed, String unit) {
        this.speed = speed;
        this.unit = unit;
    }

    /**
     * Creates {@link Wind} object with correctness check.
     * @param speed the speed
     * @param unit the unitSystem
     * @return wind object
     */
    public static Wind withValue(double speed, String unit) {
        if (speed < 0) {
            throw new InvalidWeatherValueException("Wind speed value must be in positive or zero.");
        }
        if (unit == null) {
            throw new InvalidWeatherValueException("Unit must be set.");
        }
        return new Wind(speed, unit);
    }

    /**
     * Gets speed.
     *
     * @return the speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets speed.
     *
     * @param speed the speed
     */
    public void setSpeed(double speed) {
        if (speed < 0) {
            throw new InvalidWeatherValueException("Wind speed value must be in positive or zero. Given: " + speed);
        }
        this.speed = speed;
    }

    /**
     * Gets gust value.
     * @return the gust
     */
    public Double getGust() {
        return gust;
    }

    /**
     * Sets gust value.
     * @param gust the gust.
     */
    public void setGust(double gust) {
        if (gust < 0) {
            throw new InvalidWeatherValueException("Gust speed value must be in positive or zero. Given: " + gust);
        }
        this.gust = gust;
    }

    /**
     * Gets degrees.
     *
     * @return the degrees
     */
    public Double getDegrees() {
        return degrees;
    }

    /**
     * Sets degrees.
     *
     * @param degrees the degrees
     */
    public void setDegrees(double degrees) {
        if (degrees < 0 || degrees > 360)  {
            throw new InvalidWeatherValueException("Wind direction value must be in [0, 360] range. Given: " + degrees);
        }
        this.degrees = degrees;
    }

    /**
     * Gets unitSystem.
     *
     * @return the unitSystem
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets unitSystem.
     *
     * @param unit the unitSystem
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
        if (!(o instanceof Wind wind)) return false;
        return Double.compare(wind.speed, speed) == 0 &&
                Objects.equals(degrees, wind.degrees) &&
                Objects.equals(gust, wind.gust) &&
                Objects.equals(unit, wind.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(speed, degrees, gust, unit);
    }

    @Override
    public String toString() {
        String output = "Wind speed: " + speed + " " + unit +
                ", degrees: " + degrees;
        if (gust != null) {
            output += ", Gust: " + gust + " " + unit;
        }
        return output;
    }
}
