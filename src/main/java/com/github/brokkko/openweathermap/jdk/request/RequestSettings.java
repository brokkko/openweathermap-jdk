package com.github.brokkko.openweathermap.jdk.request;

import com.github.brokkko.openweathermap.jdk.enums.Language;
import com.github.brokkko.openweathermap.jdk.enums.SdkMode;
import com.github.brokkko.openweathermap.jdk.enums.ResponseType;
import com.github.brokkko.openweathermap.jdk.enums.UnitSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestSettings {
    private static final String LANG_PARAM_NAME = "lang";
    private static final String UNITS_PARAM_NAME = "units";
    private static final String RESPONSE_TYPE_PARAM_NAME = "mode";
    private static final String API_KEY_PARAM_NAME = "appid";

    private final Map<String, String> requestParameters = new HashMap<>(8);
    private final StringBuilder urlBuilder = new StringBuilder();
    private SdkMode mode;
    private Language language;
    private UnitSystem unitSystem;

    public RequestSettings(String apiKey) {
        this.putRequestParameter(API_KEY_PARAM_NAME, apiKey);
    }

    public void putRequestParameter(String key, String value) {
        this.requestParameters.put(key, value);
    }

    public void removeRequestParameter(String key) {
        this.requestParameters.remove(key);
    }

    public Map<String, String> getRequestParameters() {
        return requestParameters;
    }

    public void appendToURL(String appendix) {
        urlBuilder.append(appendix);
    }

    public StringBuilder getUrlBuilder() {
        return urlBuilder;
    }

    public UnitSystem getUnitSystem() {
        return unitSystem;
    }

    public void setUnitSystem(UnitSystem unitSystem) {
        this.putRequestParameter(UNITS_PARAM_NAME, unitSystem.getValue());
        this.unitSystem = unitSystem;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.putRequestParameter(LANG_PARAM_NAME, language.getValue());
        this.language = language;
    }

    public void setResponseType(ResponseType responseType) {
        this.putRequestParameter(RESPONSE_TYPE_PARAM_NAME, responseType.getValue());
    }

    public String cacheKey() {
        return requestParameters.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
    }

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
