package com.github.brokkko.openweathermap.jdk.exceptions;

public class RetryExhaustedException extends RetryException {
  public RetryExhaustedException(int attempts, Throwable cause) {
    super("Retry attempts exceeded (" + attempts + " attempts)", cause);
  }
}
