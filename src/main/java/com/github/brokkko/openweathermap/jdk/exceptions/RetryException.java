package com.github.brokkko.openweathermap.jdk.exceptions;

/**
 * Base exception for retry-related errors in the SDK.
 */
public class RetryException extends WeatherSdkException {
    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }
}
