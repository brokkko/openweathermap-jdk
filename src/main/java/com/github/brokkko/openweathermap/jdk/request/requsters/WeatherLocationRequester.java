package com.github.brokkko.openweathermap.jdk.request.requsters;

import com.github.brokkko.openweathermap.jdk.clients.OpenWeatherMapClient;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.models.Coordinate;
import com.github.brokkko.openweathermap.jdk.request.RequestSettings;
import com.github.brokkko.openweathermap.jdk.request.customizers.WeatherResultCustomizer;

/**
 * Provides fluent entry points for requesting weather data by location:
 * by city name or geographic coordinates.
 */
public class WeatherLocationRequester {

    private final RequestSettings requestSettings;
    private final OpenWeatherMapClient client;
    private final WeatherLogger logger;

    /**
     * Creates a new location request entry point.
     *
     * @param client          API client.
     * @param logger          logger.
     * @param requestSettings request configuration.
     */
    public WeatherLocationRequester(OpenWeatherMapClient client, WeatherLogger logger, RequestSettings requestSettings) {
        this.requestSettings = requestSettings;
        this.requestSettings.appendToURL("/weather");
        this.client = client;
        this.logger = logger;
    }

    /**
     * Sets location by city name (e.g. "London").
     *
     * @param name city name.
     * @return a customizer to configure extra parameters.
     */
    public WeatherResultCustomizer byCityName(String name) {
        this.requestSettings.putRequestParameter("q", name);
        return new WeatherResultCustomizer(this. client, this.logger, this.requestSettings);
    }

    /**
     * Sets location by coordinates.
     *
     * @param coordinate coordinate with latitude and longitude.
     * @return a customizer to continue the request configuration.
     */
    public WeatherResultCustomizer byCoordinates(Coordinate coordinate) {
        requestSettings.putRequestParameter("lat", String.valueOf(coordinate.getLatitude()));
        requestSettings.putRequestParameter("lon", String.valueOf(coordinate.getLongitude()));
        return new WeatherResultCustomizer(this.client, this.logger, this.requestSettings);
    }
}
