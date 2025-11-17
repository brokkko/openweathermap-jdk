package com.github.brokkko.openweathermap.jdk.exceptions;

/**
 * Exception thrown when the SDK encounters an invalid or unexpected weather value.
 * This is a specific type of {@link WeatherSdkException}.
 */
public class InvalidWeatherValueException extends WeatherSdkException {
    public InvalidWeatherValueException(String message) {
        super(message);
    }
}
