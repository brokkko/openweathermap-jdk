package com.github.brokkko.openweathermap.jdk.exceptions;

/**
 * Exception thrown when the SDK encounters an invalid or unexpected weather value.
 * This is a specific type of {@link WeatherSdkException}.
 */
public class InvalidWeatherValueException extends WeatherSdkException {

    /**
     * Creates a new {@code InvalidWeatherValueException}.
     *
     * @param message description of value condition
     */
    public InvalidWeatherValueException(String message) {
        super(message);
    }
}
