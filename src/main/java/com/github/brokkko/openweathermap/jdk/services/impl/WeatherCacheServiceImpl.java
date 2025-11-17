package com.github.brokkko.openweathermap.jdk.services.impl;

import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.request.RequestSettings;
import com.github.brokkko.openweathermap.jdk.services.WeatherCacheService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.brokkko.openweathermap.jdk.constants.LogMessages.CACHE_PUT_MESSAGE;
import static com.github.brokkko.openweathermap.jdk.constants.LogMessages.CACHE_REMOVE_MESSAGE;

/**
 * Thread-safe in-memory cache storing weather API responses.
 * <p>
 * Supports TTL expiration and a size limit. When the capacity is exceeded,
 * the oldest entry is evicted. Each entry stores:
 * <ul>
 *   <li>JSON response</li>
 *   <li>timestamp of when it was fetched</li>
 *   <li>the {@link RequestSettings} associated with the response</li>
 * </ul>
 * </p>
 */
public class WeatherCacheServiceImpl implements WeatherCacheService {
    private final int capacity;
    private final long ttlMillis;

    private final ConcurrentHashMap<String, CacheEntry> store = new ConcurrentHashMap<>();
    private final WeatherLogger logger;

    /**
     * Creates a new cache.
     *
     * @param capacity  maximum number of entries before eviction occurs
     * @param ttlMillis time-to-live for each entry in milliseconds
     * @param logger    logger for cache events (insert / remove)
     */
    public WeatherCacheServiceImpl(int capacity, long ttlMillis, WeatherLogger logger) {
        this.capacity = capacity;
        this.ttlMillis = ttlMillis;
        this.logger = logger;
    }

    private record CacheEntry(String json, long fetchedAtMillis, RequestSettings settings) {
    }

    /**
     * Retrieves a cached value by key.
     * If the entry has expired (based on TTL), it is removed and {@link Optional#empty()} is returned.
     */
    @Override
    public Optional<String> get(String cacheKey) {
        CacheEntry entry = store.get(cacheKey);
        if (entry == null) return Optional.empty();
        if (System.currentTimeMillis() - entry.fetchedAtMillis > ttlMillis) {
            store.remove(cacheKey, entry);
            return Optional.empty();
        }
        return Optional.of(entry.json);
    }

    /**
     * Stores a new response. If capacity is exceeded, evicts the oldest entry.
     * Logs insertion.
     */
    @Override
    public void put(String cacheKey, String jsonResponse, RequestSettings settings) {
        if (store.size() >= capacity && !store.containsKey(cacheKey)) {
            String oldestKey = null;
            long oldestTime = Long.MAX_VALUE;
            for (Map.Entry<String, CacheEntry> e : store.entrySet()) {
                if (e.getValue().fetchedAtMillis < oldestTime) {
                    oldestTime = e.getValue().fetchedAtMillis;
                    oldestKey = e.getKey();
                }
            }
            if (oldestKey != null) {
                store.remove(oldestKey);
            }
        }
        store.put(cacheKey, new CacheEntry(jsonResponse, System.currentTimeMillis(), settings));
        logger.info(String.format(CACHE_PUT_MESSAGE, cacheKey, jsonResponse));
    }

    /**
     * Removes an entry by key. Logs removal.
     */
    @Override
    public void remove(String cacheKey) {
        store.remove(cacheKey);
        logger.info(String.format(CACHE_REMOVE_MESSAGE, cacheKey));
    }

    /**
     * Returns an immutable set of cache keys.
     */
    @Override
    public Set<String> getAllKeys() {
        return Collections.unmodifiableSet(store.keySet());
    }

    /**
     * Returns a deep snapshot mapping cache keys to their corresponding {@link RequestSettings}.
     * This snapshot is safe to modify and avoids locking the live cache during polling.
     */
    @Override
    public Map<String, RequestSettings> getAllRequestSettingsSnapshot() {
        Map<String, RequestSettings> snapshot = new HashMap<>();
        for (Map.Entry<String, CacheEntry> e : store.entrySet()) {
            snapshot.put(e.getKey(), e.getValue().settings);
        }
        return snapshot;
    }

    /**
     * Clears all entries from the cache.
     */
    @Override
    public void clear() {
        store.clear();
    }
}
