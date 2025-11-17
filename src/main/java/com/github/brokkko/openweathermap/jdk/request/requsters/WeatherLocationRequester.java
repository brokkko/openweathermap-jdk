package com.github.brokkko.openweathermap.jdk.request.requsters;

import com.github.brokkko.openweathermap.jdk.clients.OpenWeatherMapClient;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.models.Coordinate;
import com.github.brokkko.openweathermap.jdk.request.RequestSettings;
import com.github.brokkko.openweathermap.jdk.request.customizers.WeatherResultCustomizer;

public class WeatherLocationRequester {

    private final RequestSettings requestSettings;
    private final OpenWeatherMapClient client;
    private final WeatherLogger logger;

    public WeatherLocationRequester(OpenWeatherMapClient client, WeatherLogger logger, RequestSettings requestSettings) {
        this.requestSettings = requestSettings;
        this.requestSettings.appendToURL("/weather");
        this.client = client;
        this.logger = logger;
    }

    public WeatherResultCustomizer byCityName(String name) {
        this.requestSettings.putRequestParameter("q", name);
        return new WeatherResultCustomizer(this. client, this.logger, this.requestSettings);
    }

    public WeatherResultCustomizer byCoordinates(Coordinate coordinate) {
        requestSettings.putRequestParameter("lat", String.valueOf(coordinate.getLatitude()));
        requestSettings.putRequestParameter("lon", String.valueOf(coordinate.getLongitude()));
        return new WeatherResultCustomizer(this.client, this.logger, this.requestSettings);
    }
}
