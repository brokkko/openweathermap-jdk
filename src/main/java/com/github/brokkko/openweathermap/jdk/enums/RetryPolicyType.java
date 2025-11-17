package com.github.brokkko.openweathermap.jdk.enums;

/**
 * Represents types of retry policies for HTTP requests.
 */
public enum RetryPolicyType {
    /**
     * No retries will be performed. Any failure will immediately propagate as an exception.
     */
    NONE,

    /**
     * Retries failed requests using exponential backoff delays.
     * Each subsequent retry waits longer than the previous one.
     */
    EXPONENTIAL_BACKOFF
}
