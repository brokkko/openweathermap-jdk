package com.github.brokkko.openweathermap.jdk.models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a complete set of weather information observed or calculated
 * for a specific {@link Location} at a given {@link java.time.LocalDateTime}.
 * <p>
 * The class acts as an aggregated data holder combining:
 * <ul>
 *     <li>{@link WeatherState} — textual description and icon info</li>
 *     <li>{@link Temperature} — temperature and unit</li>
 *     <li>{@link AtmosphericPressure} — pressure value and unit</li>
 *     <li>{@link Humidity} — humidity percentage</li>
 *     <li>{@link Wind} — wind speed, direction, gust, units</li>
 *     <li>{@link Rain} — precipitation level</li>
 *     <li>{@link Snow} — snowfall level</li>
 *     <li>{@link Clouds} — cloudiness information</li>
 *     <li>{@link Location} — city, coordinates, country code</li>
 * </ul>
 * <p>
 */
public class Weather {

    /**
     * Timestamp representing the moment when the weather data was calculated.
     */
    private LocalDateTime calculationTime;

    private WeatherState weatherState;
    private Temperature temperature;
    private AtmosphericPressure atmosphericPressure;
    private Humidity humidity;

    private Wind wind;
    private Rain rain;
    private Snow snow;
    private Clouds clouds;

    private Location location;

    /**
     * Returns the timestamp of when the weather data was calculated.
     *
     * @return calculation time or {@code null} if not set
     */
    public LocalDateTime getCalculationTime() {
        return calculationTime;
    }

    /**
     * Sets the timestamp of when the weather data was calculated.
     *
     * @param calculationTime calculation timestamp
     */
    public void setCalculationTime(LocalDateTime calculationTime) {
        this.calculationTime = calculationTime;
    }

    /**
     * Returns weather state information.
     *
     * @return weather state or {@code null} if not set
     */
    public WeatherState getWeatherState() {
        return weatherState;
    }

    /**
     * Sets the weather state information.
     *
     * @param weatherState weather state
     */
    public void setWeatherState(WeatherState weatherState) {
        this.weatherState = weatherState;
    }

    /**
     * Returns temperature data.
     *
     * @return temperature or {@code null} if not set
     */
    public Temperature getTemperature() {
        return temperature;
    }

    /**
     * Sets temperature data.
     *
     * @param temperature temperature object
     */
    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    /**
     * Returns atmospheric pressure information.
     *
     * @return pressure or {@code null} if not set
     */
    public AtmosphericPressure getAtmosphericPressure() {
        return atmosphericPressure;
    }

    /**
     * Sets atmospheric pressure.
     *
     * @param atmosphericPressure pressure object
     */
    public void setAtmosphericPressure(AtmosphericPressure atmosphericPressure) {
        this.atmosphericPressure = atmosphericPressure;
    }

    /**
     * Returns humidity data.
     *
     * @return humidity or {@code null} if not set
     */
    public Humidity getHumidity() {
        return humidity;
    }

    /**
     * Sets humidity.
     *
     * @param humidity humidity object
     */
    public void setHumidity(Humidity humidity) {
        this.humidity = humidity;
    }

    /**
     * Returns wind information.
     *
     * @return wind or {@code null} if not set
     */
    public Wind getWind() {
        return wind;
    }

    /**
     * Sets wind data.
     *
     * @param wind wind object
     */
    public void setWind(Wind wind) {
        this.wind = wind;
    }

    /**
     * Returns rain information (precipitation).
     *
     * @return rain or {@code null} if not set
     */
    public Rain getRain() {
        return rain;
    }

    /**
     * Sets rain information.
     *
     * @param rain rain object
     */
    public void setRain(Rain rain) {
        this.rain = rain;
    }

    /**
     * Returns snow information.
     *
     * @return snow or {@code null} if not set
     */
    public Snow getSnow() {
        return snow;
    }

    /**
     * Sets snow information.
     *
     * @param snow snow object
     */
    public void setSnow(Snow snow) {
        this.snow = snow;
    }

    /**
     * Returns cloudiness information.
     *
     * @return clouds or {@code null} if not set
     */
    public Clouds getClouds() {
        return clouds;
    }

    /**
     * Sets cloudiness information.
     *
     * @param clouds clouds object
     */
    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    /**
     * Returns the associated geographical location.
     *
     * @return location or {@code null} if not set
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets location data.
     *
     * @param location location object
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weather weather)) return false;
        return Objects.equals(calculationTime, weather.calculationTime) && Objects.equals(weatherState, weather.weatherState) && Objects.equals(temperature, weather.temperature) && Objects.equals(atmosphericPressure, weather.atmosphericPressure) && Objects.equals(humidity, weather.humidity) && Objects.equals(wind, weather.wind) && Objects.equals(rain, weather.rain) && Objects.equals(snow, weather.snow) && Objects.equals(clouds, weather.clouds) && Objects.equals(location, weather.location);
    }
    @Override public int hashCode() {
        return Objects.hash(calculationTime, weatherState, temperature, atmosphericPressure, humidity, wind, rain, snow, clouds, location);
    }
    @Override public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        if (location != null) {
            stringBuilder.append("Location: ");
            stringBuilder.append(location.getName());
            final String countryCode = location.getCountryCode();
            if (countryCode != null) {
                stringBuilder.append('(');
                stringBuilder.append(countryCode);
                stringBuilder.append(')');
            }
        }
        if (weatherState != null) {
            stringBuilder.append(", Weather: ");
            stringBuilder.append(weatherState.getDescription());
        }
        if (temperature != null) {
            stringBuilder.append(", ");
            stringBuilder.append(temperature.getValue());
            stringBuilder.append(' ');
            stringBuilder.append(temperature.getUnit());
        }
        if (atmosphericPressure != null) {
            stringBuilder.append(", ");
            stringBuilder.append(atmosphericPressure.getValue());
            stringBuilder.append(' ');
            stringBuilder.append(atmosphericPressure.getUnit());
        }
        if (clouds != null) {
            stringBuilder.append(", ");
            stringBuilder.append(clouds.toString());
        }
        if (rain != null && rain.getOneHourLevel() != null) {
            stringBuilder.append(", Rain: ");
            stringBuilder.append(rain.getOneHourLevel());
            stringBuilder.append(' ');
            stringBuilder.append(rain.getUnit());
        }
        if (snow != null && snow.getOneHourLevel() != null) {
            stringBuilder.append(", Snow: ");
            stringBuilder.append(snow.getOneHourLevel());
            stringBuilder.append(' ');
            stringBuilder.append(snow.getUnit());
        }
        return stringBuilder.toString();
    }
}
