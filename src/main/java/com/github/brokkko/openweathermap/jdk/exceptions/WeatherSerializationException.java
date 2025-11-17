package com.github.brokkko.openweathermap.jdk.exceptions;

/**
 * Exception thrown when the SDK fails to parse or serialize JSON data.
 */
public class WeatherSerializationException extends WeatherSdkException {

    /**
     * Creates a new {@code WeatherSerializationException}.
     *
     * @param message description of the serialization condition
     * @param cause   the underlying cause of the serialization
     */
    public WeatherSerializationException(String message, Throwable cause) {
    super(message, cause);
  }
}
