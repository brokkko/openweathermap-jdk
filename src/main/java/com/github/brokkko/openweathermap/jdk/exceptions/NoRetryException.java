package com.github.brokkko.openweathermap.jdk.exceptions;

/**
 * Exception thrown when a retryable operation fails but retries are disabled.
 */
public class NoRetryException extends RetryException {
    public NoRetryException(Throwable cause) {
        super("Operation failed and retry is disabled", cause);
    }
}
