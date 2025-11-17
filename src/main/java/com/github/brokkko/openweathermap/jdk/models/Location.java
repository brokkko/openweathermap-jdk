package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * Represents a geographical location along with its optional metadata such as
 * country code, coordinates, sunrise/sunset times, and timezone offset.
 */
public class Location {
    private int id;
    private String name;
    private String countryCode;

    private LocalDateTime sunriseTime;
    private LocalDateTime sunsetTime;
    private ZoneOffset zoneOffset;

    private Coordinate coordinate;

    /**
     * Private constructor to enforce validation via factory method.
     */
    private Location(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Creates a {@link Location} instance after validating the location name.
     *
     * @param id   location ID
     * @param name location name, must not be null or blank
     * @return a valid {@link Location} object
     * @throws InvalidWeatherValueException if name is null or blank
     */
    public static Location withValues(int id, String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidWeatherValueException("Location name must not be null or empty.");
        }
        return new Location(id, name);
    }

    /**
     * Returns the location ID.
     *
     * @return location ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the location ID.
     *
     * @param id new ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the location name.
     *
     * @return location name
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the location name.
     *
     * @param name new location name
     * @throws InvalidWeatherValueException if name is null or blank
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidWeatherValueException("Location name must not be null or empty.");
        }
        this.name = name;
    }

    /**
     * Returns the country code.
     *
     * @return ISO country code or null
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the country code.
     *
     * @param countryCode ISO country code, may be null
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * Returns the sunrise time in the location's timezone.
     *
     * @return sunrise time
     */
    public LocalDateTime getSunriseTime() {
        return sunriseTime;
    }

    /**
     * Sets the sunrise time.
     *
     * @param sunriseTime sunrise time
     */
    public void setSunriseTime(LocalDateTime sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    /**
     * Returns the sunset time in the location's timezone.
     *
     * @return sunset time
     */
    public LocalDateTime getSunsetTime() {
        return sunsetTime;
    }

    /**
     * Sets the sunset time.
     *
     * @param sunsetTime sunset time
     */
    public void setSunsetTime(LocalDateTime sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    /**
     * Returns the timezone offset of the location.
     *
     * @return timezone offset
     */
    public ZoneOffset getZoneOffset() {
        return zoneOffset;
    }

    /**
     * Sets the timezone offset.
     *
     * @param zoneOffset timezone offset
     */
    public void setZoneOffset(ZoneOffset zoneOffset) {
        this.zoneOffset = zoneOffset;
    }

    /**
     * Returns geographical coordinates.
     *
     * @return coordinates or null
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Sets geographical coordinates.
     *
     * @param coordinate coordinate object
     */
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location location)) return false;
        return id == location.id &&
                Objects.equals(name, location.name) &&
                Objects.equals(countryCode, location.countryCode) &&
                Objects.equals(sunriseTime, location.sunriseTime) &&
                Objects.equals(sunsetTime, location.sunsetTime) &&
                Objects.equals(zoneOffset, location.zoneOffset) &&
                Objects.equals(coordinate, location.coordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, countryCode, sunriseTime, sunsetTime, zoneOffset, coordinate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (coordinate != null) {
            sb.append(coordinate).append(". ");
        }

        sb.append("ID: ").append(id)
                .append(", Name: ").append(name);

        if (countryCode != null) {
            sb.append(" (").append(countryCode).append(")");
        }

        return sb.toString();
    }
}

