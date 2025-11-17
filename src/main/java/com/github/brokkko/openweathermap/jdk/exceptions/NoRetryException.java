package com.github.brokkko.openweathermap.jdk.exceptions;

/**
 * Exception thrown when a retryable operation fails but retries are disabled.
 */
public class NoRetryException extends RetryException {

    /**
     * Creates a new {@code NoRetryException}.
     *
     * @param cause   the underlying cause
     */
    public NoRetryException(Throwable cause) {
        super("Operation failed and retry is disabled", cause);
    }
}
