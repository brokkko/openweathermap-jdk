package com.github.brokkko.openweathermap.jdk.exceptions;

/**
 * Base exception for all OpenWeatherMap SDK related errors.
 * Extends RuntimeException, so it is unchecked.
 */
public class WeatherSdkException extends RuntimeException {
  public WeatherSdkException(String message) {
    super(message);
  }

  public WeatherSdkException(String message, Throwable cause) {
    super(message, cause);
  }
}
