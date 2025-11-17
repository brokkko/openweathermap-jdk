package com.github.brokkko.openweathermap.jdk.exceptions;

/**
 * Exception thrown when all retry attempts are exhausted.
 */
public class RetryExhaustedException extends RetryException {

    /**
     * Creates a new {@code RetryExhaustedException}.
     *
     * @param attempts amount of retry attempts
     * @param cause   the underlying cause of the attempts
     */
    public RetryExhaustedException(int attempts, Throwable cause) {
    super("Retry attempts exceeded (" + attempts + " attempts)", cause);
  }
}
