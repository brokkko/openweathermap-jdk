package com.github.brokkko.openweathermap.jdk.request.customizers;

import com.github.brokkko.openweathermap.jdk.clients.OpenWeatherMapClient;
import com.github.brokkko.openweathermap.jdk.enums.Language;
import com.github.brokkko.openweathermap.jdk.enums.UnitSystem;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.request.RequestSettings;
import com.github.brokkko.openweathermap.jdk.request.terminaters.WeatherRequestTerminator;

public class WeatherResultCustomizer {
    private final RequestSettings requestSettings;
    private final OpenWeatherMapClient client;
    private final WeatherLogger logger;

    public WeatherResultCustomizer(OpenWeatherMapClient client, WeatherLogger logger, RequestSettings requestSettings) {
        this.requestSettings = requestSettings;
        this.client = client;
        this.logger = logger;
    }

    public WeatherResultCustomizer language(Language language) {
        requestSettings.setLanguage(language);
        return this;
    }

    public WeatherResultCustomizer unitSystem(UnitSystem unitSystem) {
        requestSettings.setUnitSystem(unitSystem);
        return this;
    }

    public WeatherRequestTerminator retrieve() {
        return new WeatherRequestTerminator(this.client, this.logger, this.requestSettings);
    }
}
