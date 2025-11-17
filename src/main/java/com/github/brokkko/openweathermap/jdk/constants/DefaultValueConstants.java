package com.github.brokkko.openweathermap.jdk.constants;

/**
 * A collection of default values used by the OpenWeather SDK when performing requests.
 * <p>
 * These constants define standard parameters.
 * <p>
 * The class acts as a single source of truth for default configuration values,
 * preventing duplication and ensuring consistent SDK behavior.
 */
public final class DefaultValueConstants {

    private DefaultValueConstants() {}

    /** Minimum value of polling interval. */
    public static final int DEFAULT_POLLING_INTERVAL_MIN = 5;

    /** HTTP timeout value (seconds). */
    public static final int DEFAULT_HTTP_TIMEOUT_SEC = 10;


    /** Maximum number of cache entries before eviction. */
    public static final int DEFAULT_CACHE_MAX_ENTRIES = 100;

    /** Cache TTL (ms). */
    public static final long DEFAULT_CACHE_TTL_MS = 10 * 60 * 1000L;


    /** Maximum attempts for retry policy. */
    public static final int DEFAULT_RETRY_POLICY_MAX_ATTEMPTS = 3;

    /** Retry policy delay (ms). */
    public static final int DEFAULT_RETRY_POLICY_DELAY_MS = 500;

    /** Default base URL for OpenWeatherMap API. */
    public static final String DEFAULT_BASE_URL =
            "https://api.openweathermap.org/data/2.5";
}

