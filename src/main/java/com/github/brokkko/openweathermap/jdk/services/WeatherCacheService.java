package com.github.brokkko.openweathermap.jdk.services;

import com.github.brokkko.openweathermap.jdk.request.RequestSettings;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Cache storing weather API responses.
 */
public interface WeatherCacheService {
    /**
     * Retrieves a cached value by key.
     * If the entry has expired (based on TTL), it is removed and {@link Optional#empty()} is returned.
     * @param cacheKey cache key
     * @return json entry
     */
    Optional<String> get(String cacheKey);
    /**
     * Stores a new response. If capacity is exceeded, evicts the oldest entry.
     * @param cacheKey cache key
     * @param jsonResponse json response
     * @param settings request settings
     */
    void put(String cacheKey, String jsonResponse, RequestSettings settings);
    /**
     * Removes an entry by key.
     * @param cacheKey cache key
     */
    void remove(String cacheKey);
    /**
     * Returns a set of cache keys.
     * @return set of cache keys
     */
    Set<String> getAllKeys();
    /**
     * Returns a deep snapshot mapping cache keys to their corresponding {@link RequestSettings}.
     * @return snapshot
     */
    Map<String, RequestSettings> getAllRequestSettingsSnapshot();
    /**
     * Clears all entries from the cache.
     */
    void clear();
}
