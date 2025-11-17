package com.github.brokkko.openweathermap.jdk.exceptions;

/**
 * Exception thrown when the SDK fails to parse or serialize JSON data.
 */
public class WeatherSerializationException extends WeatherSdkException {
  public WeatherSerializationException(String message, Throwable cause) {
    super(message, cause);
  }
}
