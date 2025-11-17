package com.github.brokkko.openweathermap.jdk.services;

import com.github.brokkko.openweathermap.jdk.request.RequestSettings;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface WeatherCacheService {
    Optional<String> get(String cacheKey);
    void put(String cacheKey, String jsonResponse, RequestSettings settings);
    void remove(String cacheKey);
    Set<String> getAllKeys();
    Map<String, RequestSettings> getAllRequestSettingsSnapshot();
    void clear();
}
