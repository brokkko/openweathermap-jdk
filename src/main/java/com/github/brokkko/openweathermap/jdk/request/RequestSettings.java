package com.github.brokkko.openweathermap.jdk.request;

import com.github.brokkko.openweathermap.jdk.enums.Language;
import com.github.brokkko.openweathermap.jdk.enums.SdkMode;
import com.github.brokkko.openweathermap.jdk.enums.UnitSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Holds query parameters and URL information for building OpenWeather API requests.
 * <p>
 * This class stores request parameters such as API key, language, unit system,
 * response mode, and custom query arguments. It also constructs the final URL
 * and provides a stable cache key for use in caching layers.
 */
public class RequestSettings {
    private static final String LANG_PARAM_NAME = "lang";
    private static final String UNITS_PARAM_NAME = "units";
    private static final String API_KEY_PARAM_NAME = "appid";

    private final Map<String, String> requestParameters = new HashMap<>(8);
    private final StringBuilder urlBuilder = new StringBuilder();
    private SdkMode mode;
    private Language language;
    private UnitSystem unitSystem;

    /**
     * Initializes a new instance with the required API key.
     *
     * @param apiKey API key used for OpenWeather API requests.
     */
    public RequestSettings(String apiKey) {
        this.putRequestParameter(API_KEY_PARAM_NAME, apiKey);
    }

    /**
     * Adds or replaces a request parameter that will be appended to the final query string.
     *
     * @param key   parameter name.
     * @param value parameter value.
     */
    public void putRequestParameter(String key, String value) {
        this.requestParameters.put(key, value);
    }

    /**
     * Removes a request parameter.
     *
     * @param key parameter name to remove.
     */
    public void removeRequestParameter(String key) {
        this.requestParameters.remove(key);
    }

    /**
     * Gets request parameters.
     *
     * @return a map of all configured request parameters.
     */
    public Map<String, String> getRequestParameters() {
        return requestParameters;
    }

    /**
     * Appends a new chunk to the URL path.
     *
     * @param appendix string added directly to the URL.
     */
    public void appendToURL(String appendix) {
        urlBuilder.append(appendix);
    }

    /**
     * Gets URL builder.
     *
     * @return internal URL builder used to construct the final endpoint path.
     */
    public StringBuilder getUrlBuilder() {
        return urlBuilder;
    }

    /**
     * Gets unit system.
     *
     * @return the selected unit system (metric/imperial/standard).
     */
    public UnitSystem getUnitSystem() {
        return unitSystem;
    }

    /**
     * Sets and applies the unit system for API requests.
     *
     * @param unitSystem selected unit system.
     */
    public void setUnitSystem(UnitSystem unitSystem) {
        this.putRequestParameter(UNITS_PARAM_NAME, unitSystem.getValue());
        this.unitSystem = unitSystem;
    }

    /**
     * Gets Language.
     *
     * @return the selected language for API responses.
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Sets and applies the language for API responses.
     *
     * @param language selected language.
     */
    public void setLanguage(Language language) {
        this.putRequestParameter(LANG_PARAM_NAME, language.getValue());
        this.language = language;
    }

    /**
     * Builds a stable cache key by sorting all request parameters
     * and concatenating them in a deterministic format.
     *
     * @return cache key string.
     */
    public String cacheKey() {
        return requestParameters.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
    }

    /**
     * Creates a deep copy of this object, preserving all parameters
     * and URL data while producing an independent instance.
     *
     * @return a cloned instance of RequestSettings.
     */
    public RequestSettings copy() {
        RequestSettings copy = new RequestSettings(this.requestParameters.get(API_KEY_PARAM_NAME));
        copy.urlBuilder.setLength(0);
        copy.urlBuilder.append(this.urlBuilder);
        copy.requestParameters.putAll(this.requestParameters);
        copy.mode = this.mode;
        copy.language = this.language;
        copy.unitSystem = this.unitSystem;
        return copy;
    }
}
