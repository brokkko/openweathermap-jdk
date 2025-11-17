package com.github.brokkko.openweathermap.jdk.constants;

/**
 * A set of message templates used by the SDK’s logging subsystem.
 * <p>
 * All operations related to HTTP requests, caching, retry mechanisms,
 * scheduled polling, and error handling rely on the constants defined
 * in this class to produce human-readable, consistent log messages.
 * <p>
 * Most constants are format strings intended to be used with
 * {@link String#format(String, Object...)}.
 * Centralizing all log message templates ensures consistent style and
 * simplifies debugging and log analysis.
 */
public final class LogMessages {

    private LogMessages() {}

    // --- Client lifecycle ---

    /** Message logged when a new client instance is created. */
    public static final String CLIENT_CREATED_MESSAGE =
            "Creating new OpenWeatherMapClient for key %s";

    /** Message logged when a client instance is removed from registry. */
    public static final String CLIENT_REMOVED_MESSAGE =
            "Removing OpenWeatherMapClient for key: %s";

    /** Message logged when a client is destroyed. */
    public static final String CLIENT_DESTROY_MESSAGE =
            "Destroying client for key: %s";

    // --- Polling scheduler ---

    /** Message indicating that the polling scheduler has started. */
    public static final String POLLING_STARTED_MESSAGE =
            "Polling scheduler started (interval = %d min)";

    /** Message logged when an unexpected top-level error occurs in the polling loop. */
    public static final String POLLING_TOPLEVEL_ERROR_MESSAGE =
            "Polling update error (top-level)";

    /** Message logged when the polling executor fails to shut down in time. */
    public static final String POLLING_EXECUTOR_TIMEOUT_MESSAGE =
            "Polling executor didn't terminate in time; forcing shutdown.";

    /** Message logged when a polling executor shutdown is interrupted. */
    public static final String POLLING_INTERRUPTED_MESSAGE =
            "Interrupted while shutting down polling executor";

    /** Message logged when an error occurs while clearing the cache on destroy(). */
    public static final String POLLING_CACHE_ERROR_MESSAGE =
            "Error while clearing cache during destroy()";

    // --- Polling loop messages ---

    /** Message logged when polling finds no cached entries to refresh. */
    public static final String POLLING_NO_ENTRIES_MESSAGE =
            "Polling: no cached entries to refresh.";

    /** Message logged when polling begins refreshing cached entries. */
    public static final String POLLING_REFRESHING_MESSAGE =
            "Polling: refreshing %d cached entries.";

    /** Message logged when an individual refresh operation fails. */
    public static final String POLLING_REFRESH_ERROR_MESSAGE =
            "[Polling] failed to refresh %s";

    /** Message logged when a polling refresh cycle completes. */
    public static final String POLLING_FINISHED_MESSAGE =
            "Polling: finished refresh cycle.";

    // --- Cache messages ---

    /** Message logged when a cache hit occurs in on-demand mode. */
    public static final String CACHE_HIT_ON_DEMAND_MESSAGE =
            "Cache hit (on-demand) for key: %s";

    /** Message logged when a cache miss occurs in on-demand mode. */
    public static final String CACHE_MISS_ON_DEMAND_MESSAGE =
            "Cache miss (on-demand) for key: %s — fetching from API";

    /** Message logged when a cache hit occurs during polling. */
    public static final String CACHE_HIT_POLLING_MESSAGE =
            "Cache hit (polling) for key: %s";

    /** Message logged when polling misses an entry and must fetch it synchronously. */
    public static final String CACHE_MISS_POLLING_MESSAGE =
            "Cache miss (polling) for key: %s — poller didn't populate yet, fetching synchronously";

    /** Message logged when a value is stored in cache by the poller. */
    public static final String CACHE_PUT_MESSAGE =
            "[Polling] put [%s]: %s";

    /** Message logged when a value is removed from cache by the poller. */
    public static final String CACHE_REMOVE_MESSAGE =
            "[Polling] remove [%s]";

    // --- Retry messages ---

    /** Message logged each time a retry attempt occurs. */
    public static final String RETRY_ATTEMPT_MESSAGE =
            "Executing attempt #%d";

    /** Message logged when an operation fails and will be retried. */
    public static final String RETRY_FAILED_MESSAGE =
            "Operation failed: %s. Retrying in %dms...";

    /** Message logged when all retry attempts are exhausted. */
    public static final String RETRY_EXHAUSTED_MESSAGE =
            "All retry attempts failed after %d attempts";

    // --- HTTP executor messages ---

    /** Message logged when an HTTP timeout occurs. */
    public static final String HTTP_TIMEOUT_MESSAGE =
            "HTTP timeout";

    /** Message logged when the network is unavailable. */
    public static final String HTTP_NETWORK_UNAVAILABLE_MESSAGE =
            "Network unavailable";

    /** Message logged when an I/O error occurs during HTTP communication. */
    public static final String HTTP_IO_ERROR_MESSAGE =
            "I/O error during HTTP request";

    /** Message logged when an HTTP execution thread is interrupted. */
    public static final String HTTP_INTERRUPTED_MESSAGE =
            "Execution interrupted";

    /** Message logged when the API returns an unexpected HTTP status code. */
    public static final String HTTP_UNEXPECTED_STATUS_MESSAGE =
            "HTTP %d from API";

    // --- Serialization ---

    /** Message logged when JSON parsing fails. */
    public static final String JSON_PARSE_ERROR_MESSAGE =
            "Unable to parse json";

    /** Message logged when Weather JSON cannot be parsed. */
    public static final String JSON_WEATHER_PARSE_FAILED_MESSAGE =
            "Failed to parse Weather JSON";

    /** Message logged when mapping JSON to a Weather object fails. */
    public static final String UNABLE_PARSE_JSON_TO_WEATHER_OBJECT_MESSAGE =
            "Serialization error while trying to map json to Weather.";

    // --- Terminator ---

    /** Message logged when an unexpected error occurs while fetching weather data. */
    public static final String TERMINATOR_UNEXPECTED_MESSAGE =
            "Unexpected error while getting weather for key: %s";
}
