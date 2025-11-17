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

    public static final int DEFAULT_POLLING_INTERVAL_MIN = 5;
    public static final int DEFAULT_HTTP_TIMEOUT_SEC = 10;

    public static final int DEFAULT_CACHE_MAX_ENTRIES = 100;
    public static final long DEFAULT_CACHE_TTL_MS = 10 * 60 * 1000L;

    public static final int DEFAULT_RETRY_POLICY_MAX_ATTEMPTS = 3;
    public static final int DEFAULT_RETRY_POLICY_DELAY_MS = 500;

    public static final String DEFAULT_BASE_URL =
            "https://api.openweathermap.org/data/2.5";
}

