package com.github.brokkko.openweathermap.jdk.exceptions;

/**
 * Base exception for retry-related errors in the SDK.
 */
public class RetryException extends WeatherSdkException {

    /**
     * Creates a new {@code RetryExhaustedException}.
     *
     * @param message description of retry condition
     * @param cause   the underlying cause of the retry
     */
    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }
}
