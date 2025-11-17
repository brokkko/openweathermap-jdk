package com.github.brokkko.openweathermap.jdk.exceptions;

/**
 * Exception thrown when a network or HTTP request times out.
 */
public class WeatherTimeoutException extends WeatherSdkException {

  public WeatherTimeoutException(String message, Throwable cause) {
    super(message, cause);
  }
}
