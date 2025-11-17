package com.github.brokkko.openweathermap.jdk.constants;

public final class LogMessages {

    private LogMessages() {}

    // --- Client lifecycle ---
    public static final String CLIENT_CREATED_MESSAGE =
            "Creating new OpenWeatherMapClient for key %s";
    public static final String CLIENT_REMOVED_MESSAGE =
            "Removing OpenWeatherMapClient for key: %s";
    public static final String CLIENT_DESTROY_MESSAGE =
            "Destroying client for key: %s";

    // --- Polling scheduler ---
    public static final String POLLING_STARTED_MESSAGE =
            "Polling scheduler started (interval = %d min)";
    public static final String POLLING_TOPLEVEL_ERROR_MESSAGE =
            "Polling update error (top-level)";
    public static final String POLLING_EXECUTOR_TIMEOUT_MESSAGE =
            "Polling executor didn't terminate in time; forcing shutdown.";
    public static final String POLLING_INTERRUPTED_MESSAGE =
            "Interrupted while shutting down polling executor";
    public static final String POLLING_CACHE_ERROR_MESSAGE =
            "Error while clearing cache during destroy()";

    // --- Polling loop messages ---
    public static final String POLLING_NO_ENTRIES_MESSAGE =
            "Polling: no cached entries to refresh.";
    public static final String POLLING_REFRESHING_MESSAGE =
            "Polling: refreshing %d cached entries.";
    public static final String POLLING_REFRESH_ERROR_MESSAGE =
            "[Polling] failed to refresh %s";
    public static final String POLLING_FINISHED_MESSAGE =
            "Polling: finished refresh cycle.";

    // --- Cache messages ---
    public static final String CACHE_HIT_ON_DEMAND_MESSAGE =
            "Cache hit (on-demand) for key: %s";
    public static final String CACHE_MISS_ON_DEMAND_MESSAGE =
            "Cache miss (on-demand) for key: %s — fetching from API";

    public static final String CACHE_HIT_POLLING_MESSAGE =
            "Cache hit (polling) for key: %s";
    public static final String CACHE_MISS_POLLING_MESSAGE =
            "Cache miss (polling) for key: %s — poller didn't populate yet, fetching synchronously";

    public static final String CACHE_PUT_MESSAGE =
            "[Polling] put [%s]: %s";
    public static final String CACHE_REMOVE_MESSAGE =
            "[Polling] remove [%s]";

    // --- Retry messages ---
    public static final String RETRY_ATTEMPT_MESSAGE =
            "Executing attempt #%d";
    public static final String RETRY_FAILED_MESSAGE =
            "Operation failed: %s. Retrying in %dms...";
    public static final String RETRY_EXHAUSTED_MESSAGE =
            "All retry attempts failed after %d attempts";

    // --- HTTP executor messages ---
    public static final String HTTP_TIMEOUT_MESSAGE =
            "HTTP timeout";
    public static final String HTTP_NETWORK_UNAVAILABLE_MESSAGE =
            "Network unavailable";
    public static final String HTTP_IO_ERROR_MESSAGE =
            "I/O error during HTTP request";
    public static final String HTTP_INTERRUPTED_MESSAGE =
            "Execution interrupted";
    public static final String HTTP_UNEXPECTED_STATUS_MESSAGE =
            "HTTP %d from API";

    // --- Serialization ---
    public static final String JSON_PARSE_ERROR_MESSAGE =
            "Unable to parse json";
    public static final String JSON_WEATHER_PARSE_FAILED_MESSAGE =
            "Failed to parse Weather JSON";
    public static final String UNABLE_PARSE_JSON_TO_WEATHER_OBJECT_MESSAGE =
            "Serialization error while trying to map json to Weather.";

    // --- Terminator ---
    public static final String TERMINATOR_UNEXPECTED_MESSAGE =
            "Unexpected error while getting weather for key: %s";
}

