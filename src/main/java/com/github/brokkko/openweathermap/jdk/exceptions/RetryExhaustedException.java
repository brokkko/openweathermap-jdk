package com.github.brokkko.openweathermap.jdk.exceptions;

/**
 * Exception thrown when all retry attempts are exhausted.
 */
public class RetryExhaustedException extends RetryException {
  public RetryExhaustedException(int attempts, Throwable cause) {
    super("Retry attempts exceeded (" + attempts + " attempts)", cause);
  }
}
