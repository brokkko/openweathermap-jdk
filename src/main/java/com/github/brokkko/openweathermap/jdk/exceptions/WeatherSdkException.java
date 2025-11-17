package com.github.brokkko.openweathermap.jdk.exceptions;

/**
 * Base exception for all OpenWeatherMap SDK related errors.
 * Extends RuntimeException, so it is unchecked.
 */
public class WeatherSdkException extends RuntimeException {

    /**
     * Creates a new {@code WeatherSdkException}.
     *
     * @param message description
     */
    public WeatherSdkException(String message) {
    super(message);
  }

    /**
     * Creates a new {@code WeatherSdkException}.
     *
     * @param message description
     * @param cause   the underlying cause
     */
    public WeatherSdkException(String message, Throwable cause) {
    super(message, cause);
  }
}
