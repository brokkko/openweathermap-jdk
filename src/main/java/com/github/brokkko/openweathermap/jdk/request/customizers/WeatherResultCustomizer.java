package com.github.brokkko.openweathermap.jdk.request.customizers;

import com.github.brokkko.openweathermap.jdk.clients.OpenWeatherMapClient;
import com.github.brokkko.openweathermap.jdk.enums.Language;
import com.github.brokkko.openweathermap.jdk.enums.UnitSystem;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.request.RequestSettings;
import com.github.brokkko.openweathermap.jdk.request.terminaters.WeatherRequestTerminator;

/**
 * A fluent builder that allows the caller to specify optional request parameters
 * (language, unit system) before executing the weather request.
 */
public class WeatherResultCustomizer {
    private final RequestSettings requestSettings;
    private final OpenWeatherMapClient client;
    private final WeatherLogger logger;

    /**
     * Creates a new customizer stage in the request chain.
     *
     * @param client           OpenWeatherMap client.
     * @param logger           logger used for debug and error messages.
     * @param requestSettings  active request settings.
     */
    public WeatherResultCustomizer(OpenWeatherMapClient client, WeatherLogger logger, RequestSettings requestSettings) {
        this.requestSettings = requestSettings;
        this.client = client;
        this.logger = logger;
    }

    /**
     * Sets the language for the request.
     *
     * @param language desired response language.
     * @return this builder for chaining.
     */
    public WeatherResultCustomizer language(Language language) {
        requestSettings.setLanguage(language);
        return this;
    }

    /**
     * Sets the unit system (metric/imperial/standard).
     *
     * @param unitSystem user preferred unit system.
     * @return this builder for chaining.
     */
    public WeatherResultCustomizer unitSystem(UnitSystem unitSystem) {
        requestSettings.setUnitSystem(unitSystem);
        return this;
    }

    /**
     * Finalizes the builder chain and returns the terminator,
     * which performs the actual API call or cache lookup.
     *
     * @return request terminator.
     */
    public WeatherRequestTerminator retrieve() {
        return new WeatherRequestTerminator(this.client, this.logger, this.requestSettings);
    }
}
