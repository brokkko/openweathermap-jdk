package com.github.brokkko.openweathermap.jdk.exceptions;

/**
 * Exception thrown when a network or HTTP request times out.
 */
public class WeatherTimeoutException extends WeatherSdkException {

    /**
     * Creates a new {@code WeatherTimeoutException}.
     *
     * @param message description of the timeout condition
     * @param cause   the underlying cause of the timeout
     */
  public WeatherTimeoutException(String message, Throwable cause) {
    super(message, cause);
  }
}
