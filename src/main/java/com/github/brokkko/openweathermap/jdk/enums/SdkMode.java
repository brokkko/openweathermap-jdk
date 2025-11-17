package com.github.brokkko.openweathermap.jdk.enums;

/**
 * Represents sdk modes.
 */
public enum SdkMode {
    /**
     * On-demand mode: the client executes HTTP requests immediately when requested.
     * Cached values are used if available and fresh.
     */
    ON_DEMAND,

    /**
     * Polling mode: the client periodically refreshes data in the background and caches it.
     * Requests typically read from cache for faster responses.
     */
    POLLING_MODE;
}
