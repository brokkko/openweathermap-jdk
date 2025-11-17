package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;

import java.util.Objects;

/**
 * Represents a geographical coordinate consisting of latitude and longitude.
 * Latitude must be within [-90, 90], and longitude must be within [-180, 180].
 */
public class Coordinate {
    private double latitude;
    private double longitude;

    private Coordinate() {}

    /**
     * Creates a {@link Coordinate} instance after validating latitude and longitude values.
     *
     * @param latitude  the latitude value.
     * @param longitude the longitude value.
     * @return a valid {@link Coordinate} instance.
     * @throws InvalidWeatherValueException if either value is outside the allowed range.
     */
    public static Coordinate of(double latitude, double longitude) {
        final Coordinate coordinate = new Coordinate();
        coordinate.setLatitude(latitude);
        coordinate.setLongitude(longitude);
        return coordinate;
    }

    /**
     * Sets the latitude value after validating it.
     *
     * @param latitude latitude value.
     * @throws InvalidWeatherValueException if the value is outside [-90, 90].
     */
    public void setLatitude(double latitude) {
        if (latitude < -90 || latitude > 90) {
            throw new InvalidWeatherValueException(
                    "Latitude value must be in the next range: [-90.0; 90.0]. Given: " + latitude
            );
        }
        this.latitude = latitude;
    }

    /**
     * Sets the longitude value after validating it.
     *
     * @param longitude longitude value.
     * @throws InvalidWeatherValueException if the value is outside [-180, 180].
     */
    public void setLongitude(double longitude) {
        if (longitude < -180 || longitude > 180) {
            throw new InvalidWeatherValueException(
                    "Longitude value must be in the next range: [-180.0; 180.0]. Given: " + longitude
            );
        }
        this.longitude = longitude;
    }

    /**
     * Returns latitude.
     *
     * @return latitude value.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns longitude.
     *
     * @return longitude value.
     */
    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate that)) return false;
        return Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return formatAsDegree(latitude) +
                ", " + formatAsDegree(longitude);
    }

    private String formatAsDegree(double value) {
        int degrees = (int) value;
        double secondsDouble = value % 1 * 60;
        int minutes = (int) secondsDouble;
        int seconds = (int) (secondsDouble % 1 * 60);

        return String.format("%s° %s′ %s″", degrees, minutes, seconds);
    }
}

